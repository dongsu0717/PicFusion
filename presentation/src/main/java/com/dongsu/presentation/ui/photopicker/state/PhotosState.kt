package com.dongsu.presentation.ui.photopicker.state

import com.dongsu.domain.model.Photo

data class PhotosState(
    val isLoading: Boolean = false,
    val photoList: List<Photo> = emptyList(),
    val error: Throwable? = null
)