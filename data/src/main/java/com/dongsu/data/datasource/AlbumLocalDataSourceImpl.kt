package com.dongsu.data.datasource

import android.os.Build
import com.dongsu.data.local.Gallery
import com.dongsu.data.utils.ALL_ALBUM_ID
import com.dongsu.domain.model.Album
import com.dongsu.domain.model.Photo
import javax.inject.Inject

class AlbumLocalDataSourceImpl @Inject constructor(
    private val gallery: Gallery
): AlbumLocalDataSource {
    override suspend fun loadAlbums(): List<Album> =
        try {
            val albums = gallery.getAlbums()
            if (albums.isEmpty()) {
                throw IllegalStateException("앨범 없는데?")
            }
            albums
        }catch (e: Exception) {
            throw Exception(e)
        }

    override suspend fun loadPhotos(albumId: String): List<Photo> =
        try {
            val photos = when(albumId) {
                ALL_ALBUM_ID.toString() -> gallery.getAllPhotos() //절대 이렇게 X- Preferences or DataStore
                else -> gallery.getPhotos(albumId)
            }
            if (photos.isEmpty()) {
                throw IllegalStateException("사진 없는데?")
            }
            photos
        } catch (e: Exception) {
            throw Exception(e)
        }

    override suspend fun loadAllPhotos(): List<Photo> =
        try{
            val photos = gallery.getAllPhotos()
            if(photos.isEmpty()) {
                throw IllegalStateException("사진 없는데?")
            }
            photos
        } catch (e: Exception) {
            throw Exception(e)
        }

    override suspend fun savePhoto(photoToSave: String): Unit =
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gallery.savePhotoAfterApi29(photoToSave)
            } else {
                gallery.savePhotoUnderApi29(photoToSave)
            }
        } catch(e: Exception) {
            throw Exception(e)
        }

}

