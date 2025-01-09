package com.dongsu.presentation.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dongsu.presentation.R
import com.dongsu.presentation.common.THUMBNAIL_LIST_IMAGE_HEIGHT
import com.dongsu.presentation.common.THUMBNAIL_LIST_IMAGE_WIDTH
import com.dongsu.presentation.common.bitmapToThumbnail

@Composable
fun SvgWithFrameBox(
    svgBitmap: Bitmap,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.svg_lectangle), //svg 틀 이미지
            contentDescription = "PNG 템플릿",
            modifier = Modifier.fillMaxSize()
        )
        Image(
            bitmap = svgBitmap.asImageBitmap(),
//            bitmapToThumbnail(svgBitmap, THUMBNAIL_LIST_IMAGE_WIDTH, THUMBNAIL_LIST_IMAGE_HEIGHT).asImageBitmap(), //썸네일로 변환 후 로드
            contentDescription = "합성",
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clickable {
                    onClick()
                }
        )
    }
}