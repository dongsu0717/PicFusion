package com.dongsu.data.datasource

import com.dongsu.domain.model.Photo

interface CacheLocalDataSource {
    suspend fun savePhotoListToCache(photoList: List<Photo>): Unit
}