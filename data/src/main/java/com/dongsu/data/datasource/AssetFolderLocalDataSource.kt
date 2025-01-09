package com.dongsu.data.datasource

import com.dongsu.domain.model.Asset

interface AssetFolderLocalDataSource {
    suspend fun loadAllAssetsFiles(): List<Asset>
}