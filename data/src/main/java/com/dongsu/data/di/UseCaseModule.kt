package com.dongsu.data.di

import com.dongsu.domain.usecase.SaveAllImagesToCacheUseCase
import com.dongsu.domain.usecase.SaveAllImagesToCacheUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAlbumUseCase(
        saveAllImagesToCacheUseCaseImpl: SaveAllImagesToCacheUseCaseImpl
    ): SaveAllImagesToCacheUseCase

}