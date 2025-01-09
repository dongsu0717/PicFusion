package com.dongsu.presentation.ui.overlay.content

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.dongsu.presentation.R
import com.dongsu.presentation.ui.components.SvgWithFrameBox

@Composable
fun SvgRowContent(
    svgBitmap: List<Bitmap>?,
    onClick: (Bitmap) -> Unit,
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding))
    ) {
        svgBitmap?.let { svgList ->
            items(svgList.size) { svg ->
                SvgWithFrameBox(
                    svgBitmap[svg],
                    onClick = { onClick(svgList[svg]) }
                )
            }
        }
    }
}
