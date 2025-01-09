package com.dongsu.presentation.ui.photopicker.intent

sealed class PhotosEvent {
    data class LoadPhotos(val albumId: String) : PhotosEvent()
}