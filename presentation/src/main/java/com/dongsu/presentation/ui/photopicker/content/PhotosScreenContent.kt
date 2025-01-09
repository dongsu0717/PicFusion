package com.dongsu.presentation.ui.photopicker.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dongsu.domain.model.Photo
import com.dongsu.presentation.R
import com.dongsu.presentation.common.PHOTO_LIST_CELL
import com.dongsu.presentation.common.THUMBNAIL_LIST_IMAGE_HEIGHT
import com.dongsu.presentation.common.THUMBNAIL_LIST_IMAGE_WIDTH
import com.dongsu.presentation.common.UTF_8
import com.dongsu.presentation.common.checkMemoryUsage
import com.dongsu.presentation.navigation.Destination
import com.dongsu.presentation.ui.components.MainTopAppBar
import java.net.URLEncoder

@Composable
fun PhotosScreenContent(
    photoList: List<Photo>,
    albumName: String?,
    navController: NavController
) {

    SideEffect {
        checkMemoryUsage()
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                navController = navController,
                title = albumName!!,
                showBackButton = true,
                shadow = true
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(PHOTO_LIST_CELL),
            modifier = Modifier.padding(paddingValues)
                .padding(top = dimensionResource(R.dimen.large_padding)),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(photoList) { photo ->

                // Coil 이용 X
                /*
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }

                // 비등기로 하나씩 비트맵 로드
                LaunchedEffect(photo.id) {
                    bitmap = getBitmapAsync(photo.id.toString(), photo.uri)
                }

                bitmap?.let {
                    Image(
                        bitmap = bitmapToThumbnail(it, THUMBNAIL_LIST_IMAGE_WIDTH, THUMBNAIL_LIST_IMAGE_HEIGHT).asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                val encodedPhotoUri = URLEncoder.encode(photo.uri, UTF_8)
                                navController.navigate("${Destination.Edit.route}/$encodedPhotoUri/$albumName")
                            },
                        contentScale = ContentScale.Crop
                    )*/

                    // Coil이용 O
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.uri)
//                                .size(THUMBNAIL_LIST_IMAGE_WIDTH, THUMBNAIL_LIST_IMAGE_HEIGHT)
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                val encodedPhotoUri = URLEncoder.encode(photo.uri, UTF_8)
                                navController.navigate(Destination.Edit.route + "/$encodedPhotoUri" +"/$albumName")
                            },
                        contentScale = ContentScale.Crop
                    )


            }
        }
    }
}

