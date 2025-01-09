package com.dongsu.data.repository

import com.dongsu.data.datasource.CacheLocalDataSource
import com.dongsu.domain.model.Photo
import com.dongsu.domain.repository.CacheRepository
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
    private val cacheLocalDataSource: CacheLocalDataSource
): CacheRepository {
    override suspend fun savePhotoListToCache(photoList: List<Photo>): Result<Unit> = runCatching {
        cacheLocalDataSource.savePhotoListToCache(photoList)
    }
}