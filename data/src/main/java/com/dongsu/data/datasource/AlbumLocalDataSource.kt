package com.dongsu.data.datasource

import com.dongsu.domain.model.Album
import com.dongsu.domain.model.Photo

interface AlbumLocalDataSource {
    suspend fun loadAlbums(): List<Album>
    suspend fun loadPhotos(albumId: String): List<Photo>
    suspend fun loadAllPhotos(): List<Photo>
    suspend fun savePhoto(photoToSave: String)
}