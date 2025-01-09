package com.dongsu.data.datasource

import com.dongsu.data.local.AssetsFolder
import com.dongsu.domain.model.Asset
import javax.inject.Inject

class AssetFolderLocalDataSourceImpl @Inject constructor(
    private val assetsFolder: AssetsFolder,
) : AssetFolderLocalDataSource {
    override suspend fun loadAllAssetsFiles(): List<Asset> =
        try {
            val assets = assetsFolder.getAllSvgAssets()
            if (assets.isEmpty()) {
                throw IllegalStateException("svg 없는데?")
            }
            assets
        } catch (e: Exception) {
            throw e
        }
}