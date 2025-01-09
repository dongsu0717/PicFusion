package com.dongsu.data.di

import com.dongsu.data.datasource.AlbumLocalDataSource
import com.dongsu.data.datasource.AlbumLocalDataSourceImpl
import com.dongsu.data.datasource.AssetFolderLocalDataSource
import com.dongsu.data.datasource.AssetFolderLocalDataSourceImpl
import com.dongsu.data.datasource.CacheLocalDataSource
import com.dongsu.data.datasource.CacheLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAlbumLocalDataSource(
        albumLocalDataSourceImpl: AlbumLocalDataSourceImpl
    ): AlbumLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAssetFolderLocalDataSource(
        assetFolderLocalDataSourceImpl: AssetFolderLocalDataSourceImpl
    ): AssetFolderLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCacheLocalDataSource(
        cacheLocalDataSourceImpl: CacheLocalDataSourceImpl
    ): CacheLocalDataSource

}