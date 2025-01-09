package com.dongsu.data.local

import android.content.Context
import com.dongsu.domain.model.Asset
import javax.inject.Inject

class AssetsFolder @Inject constructor(
    private val context: Context,
) {
    fun getAllSvgAssets(): List<Asset> {
        val assetManager = context.assets
        val svgFiles = assetManager.list("")?.filter { it.endsWith(".svg") } ?: return emptyList()

        return svgFiles.mapNotNull { fileName ->
            try {
                val svgString = assetManager.open(fileName).bufferedReader().use { it.readText() }
                Asset(content = svgString)
            } catch (e: Exception) {
                null
            }
        }
    }
}
