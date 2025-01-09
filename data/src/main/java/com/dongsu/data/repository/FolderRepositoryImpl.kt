package com.dongsu.data.repository

import com.dongsu.data.datasource.AssetFolderLocalDataSource
import com.dongsu.domain.model.Asset
import com.dongsu.domain.repository.FolderRepository
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val assetFolderLocalDataSource: AssetFolderLocalDataSource
): FolderRepository{
    override suspend fun loadAllAssetsFile(): Result<List<Asset>> = runCatching {
        assetFolderLocalDataSource.loadAllAssetsFiles()
    }
}