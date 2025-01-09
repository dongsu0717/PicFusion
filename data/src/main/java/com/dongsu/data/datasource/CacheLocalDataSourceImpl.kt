package com.dongsu.data.datasource

import com.dongsu.data.local.Cache
import com.dongsu.domain.model.Photo
import javax.inject.Inject

class CacheLocalDataSourceImpl @Inject constructor(
    private val cache: Cache
): CacheLocalDataSource {

    override suspend fun savePhotoListToCache(photoList: List<Photo>): Unit =
        try {
            cache.savePhotoListToCache(photoList)
        } catch (e: Exception) {
            throw e
        }
}