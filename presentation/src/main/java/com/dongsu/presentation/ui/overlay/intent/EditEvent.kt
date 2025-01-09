package com.dongsu.presentation.ui.overlay.intent

import android.graphics.Bitmap

sealed class EditEvent {
    data object LoadSvg : EditEvent()
    data class CompressFile(val combinedBitmap: Bitmap) : EditEvent()
    data class SavePhoto(val photo: String) : EditEvent()
    data object PermissionDenied : EditEvent()
    data object OffPermissionDialog : EditEvent()
}
