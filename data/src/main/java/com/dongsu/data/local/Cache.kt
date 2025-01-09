package com.dongsu.data.local

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.ImageView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.dongsu.data.R
import com.dongsu.domain.model.Photo
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class Cache @Inject constructor(
    private val context: Context,
) {
    // 캐시에 저장, 불러오기 Data Module -> Presentation Module로 변경
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory/8
    private val cache = LruCache<String, String>(cacheSize)

    fun saveImageToCache(key: String, imageString: String) {
        Timber.e("캐시에 저장 key: $key, imageString: $imageString")
        cache.put(key, imageString)
    }

    fun getImageFromCache(key: String): String? {
        val imageString =  cache.get(key)
        Timber.e("캐쉬에서 가져갈 key: $key, imageString: $imageString")
        return imageString
    }

    //X
    fun savePhotoListToCache(photoList: List<Photo>): Unit {
        photoList.forEach { imagePath ->
            val request = ImageRequest.Builder(context)
                .data(imagePath)
                .target(
                    onSuccess = {
                        Timber.e("이미지 캐시에 저장 성공")
                    },
                    onError = {
                        Timber.e("이미지 캐시에 저장 실패")
                    }
                )
                .build()

            imageLoader.enqueue(request)
        }
    }

    private val imageLoader = ImageLoader.Builder(context)
        .memoryCache { MemoryCache.Builder(context).build() }
        .diskCache { DiskCache.Builder().directory(File(context.cacheDir, "pixo")).build() }
        .build()
}