package com.dongsu.presentation.ui.albumlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dongsu.presentation.R
import com.dongsu.presentation.ui.albumlist.content.AlbumScreenContent
import com.dongsu.presentation.ui.albumlist.intent.AlbumEvent
import com.dongsu.presentation.ui.components.ErrorComponent
import com.dongsu.presentation.ui.components.LoadingComponent

@Composable
fun AlbumScreen(
    navController: NavController,
) {
    val albumViewModel : AlbumViewModel = hiltViewModel()
    val albumState by albumViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        albumViewModel.onEvent(AlbumEvent.LoadAlbums)
    }

    when {
        albumState.isLoading -> LoadingComponent(message = stringResource(R.string.loading))
        albumState.albumList.isNotEmpty() -> AlbumScreenContent(
            albumList = albumState.albumList,
            navController = navController
        )
        albumState.error != null -> {
            ErrorComponent(error = albumState.error)
        }
    }
}