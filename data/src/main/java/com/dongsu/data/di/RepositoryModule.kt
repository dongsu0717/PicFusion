package com.dongsu.data.di

import com.dongsu.data.repository.AlbumRepositoryImpl
import com.dongsu.data.repository.CacheRepositoryImpl
import com.dongsu.data.repository.FolderRepositoryImpl
import com.dongsu.domain.repository.AlbumRepository
import com.dongsu.domain.repository.CacheRepository
import com.dongsu.domain.repository.FolderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository

    @Binds
    @Singleton
    abstract fun provideFolderRepository(
        folderRepositoryImpl: FolderRepositoryImpl
    ): FolderRepository

    @Binds
    @Singleton
    abstract fun provideCacheRepository(
        cacheRepositoryImpl: CacheRepositoryImpl
    ): CacheRepository


}