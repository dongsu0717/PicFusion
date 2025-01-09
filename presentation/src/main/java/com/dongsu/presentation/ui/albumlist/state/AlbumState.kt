package com.dongsu.presentation.ui.albumlist.state

import com.dongsu.domain.model.Album

data class AlbumState(
    val isLoading: Boolean = false,
    val albumList: List<Album> = emptyList(),
    val isSaveAllPhotoCache : Boolean = false,
    val error: Throwable? = null
)