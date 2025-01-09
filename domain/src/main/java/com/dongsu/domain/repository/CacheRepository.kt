package com.dongsu.domain.repository

import com.dongsu.domain.model.Photo

interface CacheRepository {
    suspend fun savePhotoListToCache(photoList: List<Photo>): Result<Unit>
}