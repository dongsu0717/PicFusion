package com.dongsu.domain.repository

import com.dongsu.domain.model.Album
import com.dongsu.domain.model.Photo

interface AlbumRepository {
    suspend fun loadAlbums(): Result<List<Album>>
    suspend fun loadPhotos(albumId: String): Result<List<Photo>>
    suspend fun savePhoto(photoToSave: String): Result<Unit>
    suspend fun loadAllPhotos(): Result<List<Photo>>
}