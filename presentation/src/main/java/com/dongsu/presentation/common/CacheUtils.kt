package com.dongsu.presentation.common

import android.graphics.Bitmap
import android.os.Debug
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
private val cacheSize = maxMemory/8
private val cache = LruCache<String, Bitmap>(cacheSize)

/*
캐시확인해서 있으면 가져오고, 없으면 캐시에 넣어놓기
*/
suspend fun getBitmapAsync(photoId: String, photoUri: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        val cachedBitmap = getImageFromCache(photoId)
        cachedBitmap ?: run {
            stringToBitmap(photoUri)?.also {
                saveImageToCache(photoId, it)
            }
        }
    }
}

fun saveImageToCache(key: String, imageString: Bitmap) {
    Timber.e("캐시에 저장 key: $key, imageString: $imageString")
    cache.put(key, imageString)
}

fun getImageFromCache(key: String): Bitmap? {
    val imageString =  cache.get(key)
    Timber.e("캐쉬에서 가져갈 key: $key, imageString: $imageString")
    return imageString
}

fun checkMemoryUsage(){
    val allocatedSize = Debug.getNativeHeapAllocatedSize()
    val freeSize = Debug.getNativeHeapFreeSize()
    val usedSize = allocatedSize - freeSize
    Timber.e("메모리 할당량: $allocatedSize bytes")
    Timber.e("메모리 사용량: $freeSize bytes")
    Timber.e("메모리 사용량: $usedSize bytes")
}

