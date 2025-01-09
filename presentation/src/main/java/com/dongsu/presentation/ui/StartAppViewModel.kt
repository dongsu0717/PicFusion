package com.dongsu.presentation.ui

import android.content.Context
import com.dongsu.presentation.base.BaseViewModel
import com.dongsu.presentation.common.checkReadImagePermission
import com.dongsu.presentation.common.getGalleryPermission
import com.dongsu.presentation.common.shouldShowRequestPermissionRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class StartAppState(
    val isReadPermissionGranted: Boolean = false,
    val isReadPermissionNotGranted: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false,
    val shouldShowRationale: Boolean = false
)

sealed class StartAppEvent {
    data class CheckReadPermission(val context: Context) : StartAppEvent()
    data object PermissionGranted : StartAppEvent()
    data object PermissionDenied : StartAppEvent()
    data class RetryCheckReadPermission(val context: Context) : StartAppEvent()
}

@HiltViewModel
class StartAppViewModel @Inject constructor(

) : BaseViewModel<StartAppState, StartAppEvent>(StartAppState()) {

    override suspend fun handleEvent(event: StartAppEvent) {
        when(event){
            is StartAppEvent.CheckReadPermission -> updateUiState { copy(
                isReadPermissionGranted = checkReadImagePermission(event.context),
                isReadPermissionNotGranted = !checkReadImagePermission(event.context)
            ) }
            is StartAppEvent.PermissionGranted -> updateUiState { copy(
                isReadPermissionGranted = true,
                showPermissionDeniedDialog = false,
                shouldShowRationale = false
            ) }
            is StartAppEvent.PermissionDenied -> updateUiState { copy(
                isReadPermissionGranted = false,
                isReadPermissionNotGranted = false,
                showPermissionDeniedDialog = true,
                shouldShowRationale = false
            ) }
            is StartAppEvent.RetryCheckReadPermission -> updateUiState { copy(
                showPermissionDeniedDialog = false,
                isReadPermissionGranted = !shouldShowRequestPermissionRationale(event.context, arrayOf(getGalleryPermission())),
                shouldShowRationale = shouldShowRequestPermissionRationale(event.context, arrayOf(getGalleryPermission())),
            ) }
        }
    }
}