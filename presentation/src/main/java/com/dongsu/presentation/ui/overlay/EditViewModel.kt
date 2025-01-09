package com.dongsu.presentation.ui.overlay

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.dongsu.domain.repository.AlbumRepository
import com.dongsu.domain.repository.FolderRepository
import com.dongsu.presentation.base.BaseViewModel
import com.dongsu.presentation.common.bitmapToString
import com.dongsu.presentation.ui.overlay.intent.EditEvent
import com.dongsu.presentation.ui.overlay.state.EditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val albumRepository: AlbumRepository
):BaseViewModel<EditState, EditEvent>(EditState()) {

    override suspend fun handleEvent(event: EditEvent) {
        when(event){
            is EditEvent.LoadSvg -> loadSvg()
            is EditEvent.SavePhoto -> savePhoto(event.photo)
            is EditEvent.CompressFile -> combineImages(event.combinedBitmap)
            is EditEvent.PermissionDenied -> updateUiState { copy(
                showPermissionDeniedDialog = true
            ) }
            is EditEvent.OffPermissionDialog -> updateUiState { copy(
                showPermissionDeniedDialog = false
            ) }
        }
    }

    private fun savePhoto(photoToSave: String) {
        updateUiState { copy(isLoadingCompressing = false, isLoadingSaving = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                albumRepository.savePhoto(photoToSave)
                    .onSuccess {
                        updateUiState { copy( isPhotoSaved = true, isLoadingSaving = false) }
                    }
                    .onFailure {
                        updateUiState { copy( error = it) }
                    }
            }
        }
    }

    private fun loadSvg() {
        updateUiState { copy(isLoading = true) }
        viewModelScope.launch {
            folderRepository.loadAllAssetsFile()
                .onSuccess {
                    updateUiState { copy(svgList = it, isLoading = false) }
                }
                .onFailure {
                    updateUiState { copy(error = it, isLoading = false) }
                }
        }
    }

    private fun combineImages(combinedBitmap: Bitmap) {
        updateUiState { copy(isLoadingCompressing = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bitmapToString(combinedBitmap)
                    .onSuccess {
                        updateUiState { copy(photoToCompressed = it, isLoadingCompressing = false,) }
                    }
                    .onFailure {
                        updateUiState { copy(error = it, isLoadingCompressing = false) }
                    }

            }
        }
    }
}
