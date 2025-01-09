package com.dongsu.presentation.ui.overlay.content

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import com.dongsu.presentation.R

@Composable
fun PhotoContent(
    photo: Bitmap?,
    combinedPhoto: Bitmap?,
    modifier: Modifier = Modifier
) {
    val displayPhoto = combinedPhoto ?: photo
    displayPhoto?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = if (combinedPhoto == null) "메인 사진" else "합성된 사진",
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.large_padding))
        )
    }
}