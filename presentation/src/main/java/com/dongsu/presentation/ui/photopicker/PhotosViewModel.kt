package com.dongsu.presentation.ui.photopicker

import androidx.lifecycle.viewModelScope
import com.dongsu.domain.repository.AlbumRepository
import com.dongsu.presentation.base.BaseViewModel
import com.dongsu.presentation.ui.photopicker.intent.PhotosEvent
import com.dongsu.presentation.ui.photopicker.state.PhotosState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
): BaseViewModel<PhotosState, PhotosEvent>(PhotosState()) {

    override suspend fun handleEvent(event: PhotosEvent) {
        when (event) {
            is PhotosEvent.LoadPhotos -> loadPhotos(event.albumId)
        }
    }

    private fun loadPhotos(albumId: String) {
        updateUiState { copy(isLoading = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                albumRepository.loadPhotos(albumId)
                    .onSuccess {
                        updateUiState { copy(photoList = it, isLoading = false) }
                    }
                    .onFailure {
                        updateUiState { copy(error = it) }
                    }
            }
        }
    }
}