package com.dongsu.data.repository

import com.dongsu.data.datasource.AlbumLocalDataSource
import com.dongsu.domain.model.Album
import com.dongsu.domain.model.Photo
import com.dongsu.domain.repository.AlbumRepository
import javax.inject.Inject


class AlbumRepositoryImpl @Inject constructor(
    private val albumLocalDataSource: AlbumLocalDataSource,
): AlbumRepository {
    override suspend fun loadAlbums(): Result<List<Album>> = runCatching {
        albumLocalDataSource.loadAlbums()
    }

    override suspend fun loadPhotos(albumId: String): Result<List<Photo>> = runCatching {
        albumLocalDataSource.loadPhotos(albumId)
    }

    override suspend fun savePhoto(photoToSave: String): Result<Unit> = runCatching {
        albumLocalDataSource.savePhoto(photoToSave)
    }

    override suspend fun loadAllPhotos(): Result<List<Photo>> = runCatching {
        albumLocalDataSource.loadAllPhotos()
    }
}

