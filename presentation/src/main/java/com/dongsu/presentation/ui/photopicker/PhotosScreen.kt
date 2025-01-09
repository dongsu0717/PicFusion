package com.dongsu.presentation.ui.photopicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dongsu.presentation.ui.components.ErrorComponent
import com.dongsu.presentation.ui.components.LoadingComponent
import com.dongsu.presentation.ui.photopicker.content.PhotosScreenContent
import com.dongsu.presentation.ui.photopicker.intent.PhotosEvent

@Composable
fun PhotosScreen(
    navController: NavController,
    albumId: String?,
    albumName: String?
){
    val photosViewModel: PhotosViewModel = hiltViewModel()
    val state by photosViewModel.uiState.collectAsStateWithLifecycle()

//    SideEffect {
//        checkCoilMemoryCache(context)
//        checkMemoryUsage()
//    }

    LaunchedEffect(Unit) {
        albumId?.let { photosViewModel.onEvent(PhotosEvent.LoadPhotos(it)) }
    }

    when {
        state.isLoading -> LoadingComponent()
        state.photoList.isNotEmpty() -> {
            PhotosScreenContent(
                navController = navController,
                photoList = state.photoList,
                albumName = albumName
            )
        }
        state.error != null -> ErrorComponent(error = state.error)
    }
}