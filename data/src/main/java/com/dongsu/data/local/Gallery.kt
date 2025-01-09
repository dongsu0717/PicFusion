package com.dongsu.data.local

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.contentValuesOf
import com.dongsu.data.utils.ALL_ALBUM_ID
import com.dongsu.data.utils.All_ALBUM_NAME
import com.dongsu.data.utils.stringToBitmap
import com.dongsu.domain.model.Album
import com.dongsu.domain.model.Photo
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class Gallery @Inject constructor(
    private val context: Context,
) {

    fun getAllPhotos(): List<Photo> {
        return getPhotoUris().map { Photo(id = it.key.toInt(),uri = it.value) }
    }

    fun getPhotos(albumId: String): List<Photo> {
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId)
        return getPhotoUris(selection, selectionArgs).map { Photo(id = it.key.toInt(),uri = it.value) }
    }

    fun savePhotoAfterApi29(photoToSave: String) {
        Timber.e("사진 저장 API 29이상")

        val decodedBytes = Base64.decode(photoToSave, Base64.DEFAULT)

        val currentTime = System.currentTimeMillis()
        val contentValues = contentValuesOf(
            MediaStore.Images.Media.DISPLAY_NAME to "pixo_combined_image_$currentTime.png",
            MediaStore.Images.Media.MIME_TYPE to "image/png",
            MediaStore.Images.Media.RELATIVE_PATH to Environment.DIRECTORY_PICTURES
        )

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(getExternalContentUri(), contentValues)
            ?: throw IllegalStateException("url 생성 실패 $contentValues")

        val outputStream = contentResolver.openOutputStream(uri)
            ?: throw IllegalStateException("outputStram 오픈 실패. uri : $uri")
        outputStream.use { it.write(decodedBytes) }
    }

    fun savePhotoUnderApi29(photoToSave: String) {
        Timber.e("사진 저장 API 29미만")
        val decodedBytes = Base64.decode(photoToSave, Base64.DEFAULT)
        val currentTime = System.currentTimeMillis()

        val pictureFolder = Environment.getExternalStorageDirectory().toString() +
                "/" + Environment.DIRECTORY_PICTURES
        val imageFolder = File(pictureFolder).apply {
            if (!isDirectory) mkdirs()
        }

        val completeFileName = "/pixo_combined_image_$currentTime.png"
        val outputStream = FileOutputStream(pictureFolder + completeFileName)
        outputStream.use { it.write(decodedBytes) }
        scanPhoto(context, imageFolder.absolutePath + completeFileName)
    }


    fun getAlbums(): List<Album> {
        var albumList = mutableListOf<Album>()

        val albumMap = mutableMapOf<Int, MutableList<String>>()
        val albumNameMap = mutableMapOf<Int, String>()
        val allPhotos = mutableListOf<String>()

        getAlbumCursor()?.use {
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val albumNameColumn =
                it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val photoUriColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val albumId = it.getInt(albumIdColumn)
                val albumName = it.getString(albumNameColumn)
                val photoUri = it.getString(photoUriColumn)

                allPhotos.add(photoUri)
                albumMap.getOrPut(albumId) { mutableListOf() }.add(photoUri)
                albumNameMap[albumId] = albumName
            }
        }

        albumList = albumMap.map { (albumId, photoPaths) ->
            Album(
                albumId = albumId,
                albumCoverUrl = photoPaths.firstOrNull() ?: "",
                albumName = albumNameMap[albumId] ?: "Unknown",
                photoCount = photoPaths.size
            )
        }.toMutableList()

        val recentAlbum = Album(
            albumId = ALL_ALBUM_ID,
            albumCoverUrl = allPhotos.firstOrNull() ?: "",
            albumName = All_ALBUM_NAME,
            photoCount = allPhotos.size
        )
        albumList.add(0, recentAlbum)
        return albumList
    }

    @Suppress("DEPRECATION")
    private fun scanPhoto(context: Context, imagePath: String) {
        val intent = Intent(
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://$imagePath")
        )
        context.sendBroadcast(intent)
    }

    private fun getPhotoUris(
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Map<String, String> {
        val photoUris = mutableMapOf<String,String>()
        getAlbumCursor(selection, selectionArgs)?.use { cursor ->
            val photoIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val photoId = cursor.getString(photoIdColumn)
                val photoUri = cursor.getString(dataColumn)
                photoUris[photoId] = photoUri
            }
        }
        return photoUris
    }

    private fun getAlbumCursor(
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Cursor? {
        val externalUri = getExternalContentUri()
        val projection = getProjection()
        val sortOrder = getSelection()
        return context.contentResolver.query(
            externalUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

    private fun getExternalContentUri() = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private fun getProjection() = arrayOf(
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID
    )

    private fun getSelection() = "${MediaStore.Images.Media.DATE_ADDED} DESC"
}

