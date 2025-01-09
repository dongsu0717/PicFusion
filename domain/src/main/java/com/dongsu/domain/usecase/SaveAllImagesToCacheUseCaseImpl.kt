package com.dongsu.domain.usecase

import com.dongsu.domain.repository.AlbumRepository
import com.dongsu.domain.repository.CacheRepository
import javax.inject.Inject

class SaveAllImagesToCacheUseCaseImpl @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val cacheRepository: CacheRepository,
) : SaveAllImagesToCacheUseCase {
    override suspend fun saveAllImageToCache(): Result<Unit> = runCatching {
        albumRepository.loadAllPhotos()
            .onSuccess { photoList ->
                cacheRepository.savePhotoListToCache(photoList)
            }.onFailure {
                throw it
            }
    }
}