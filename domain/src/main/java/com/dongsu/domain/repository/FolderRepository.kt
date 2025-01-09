package com.dongsu.domain.repository

import com.dongsu.domain.model.Asset

interface FolderRepository {
    suspend fun loadAllAssetsFile(): Result<List<Asset>>
}