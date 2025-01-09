package com.dongsu.presentation.ui.albumlist

import androidx.lifecycle.viewModelScope
import com.dongsu.domain.repository.AlbumRepository
import com.dongsu.domain.usecase.SaveAllImagesToCacheUseCase
import com.dongsu.presentation.base.BaseViewModel
import com.dongsu.presentation.ui.albumlist.intent.AlbumEvent
import com.dongsu.presentation.ui.albumlist.state.AlbumState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val saveAllImagesToCacheUseCase: SaveAllImagesToCacheUseCase
): BaseViewModel<AlbumState, AlbumEvent>(AlbumState()) {

    override suspend fun handleEvent(event: AlbumEvent) {
        when (event) {
            is AlbumEvent.LoadAlbums -> loadAlbums()
            is AlbumEvent.SaveAllPhotoCache -> saveAllPhotoCache()
        }
    }

    private fun loadAlbums() {
        updateUiState { copy(isLoading = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                albumRepository.loadAlbums()
                    .onSuccess {
                        updateUiState { copy(albumList = it, isLoading = false) }
                    }
                    .onFailure {
                        updateUiState { copy(error = it) }
                    }
            }
        }
    }

    private fun saveAllPhotoCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                saveAllImagesToCacheUseCase.saveAllImageToCache()
                    .onSuccess {
                        updateUiState { copy(isSaveAllPhotoCache = true) }
                    }
                    .onFailure {
                        updateUiState { copy(error = it) }
                    }
            }
        }
    }
}