package com.dongsu.presentation.ui.albumlist.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import com.dongsu.domain.model.Album
import com.dongsu.presentation.R
import com.dongsu.presentation.common.ALBUM_LIST_CELL
import com.dongsu.presentation.navigation.Destination
import com.dongsu.presentation.ui.components.AlbumCard

@Composable
fun AlbumGrid(
    albumList: List<Album>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(ALBUM_LIST_CELL),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.default_padding))
    ) {
        items(albumList) { item ->
            AlbumCard(
                imageUrl = item.albumCoverUrl,
                title = item.albumName,
                count = item.photoCount,
                onClick = {
                    navController.navigate(
                        Destination.Photos.route +
                                "/${item.albumId}" +
                                "/${item.albumName}"
                    )
                }
            )
        }
    }
}