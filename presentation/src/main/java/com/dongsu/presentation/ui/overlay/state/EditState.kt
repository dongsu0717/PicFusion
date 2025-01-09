package com.dongsu.presentation.ui.overlay.state

import com.dongsu.domain.model.Asset

typealias StringCompressedPhoto = String

data class EditState (
    val isLoading: Boolean = false,
    val svgList: List<Asset> = emptyList(),

    val isLoadingCompressing: Boolean = false,
    val photoToCompressed: StringCompressedPhoto? = null,

    val isLoadingSaving: Boolean = false,
    val isPhotoSaved: Boolean = false,

    val error: Throwable? = null,
    val showPermissionDeniedDialog: Boolean = false,
)
