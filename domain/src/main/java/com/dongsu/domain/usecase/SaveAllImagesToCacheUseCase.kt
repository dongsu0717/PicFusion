package com.dongsu.domain.usecase

interface SaveAllImagesToCacheUseCase {
    suspend fun saveAllImageToCache(): Result<Unit>
}