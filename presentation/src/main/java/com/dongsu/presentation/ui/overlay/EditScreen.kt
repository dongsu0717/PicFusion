package com.dongsu.presentation.ui.overlay

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dongsu.presentation.R
import com.dongsu.presentation.common.CommonDialog
import com.dongsu.presentation.common.checkWritePermission
import com.dongsu.presentation.common.combineImages
import com.dongsu.presentation.common.convertSvgToBitmap
import com.dongsu.presentation.common.getWritePermission
import com.dongsu.presentation.common.openSettings
import com.dongsu.presentation.common.stringToBitmap
import com.dongsu.presentation.navigation.Destination
import com.dongsu.presentation.ui.components.ErrorComponent
import com.dongsu.presentation.ui.components.GlobalLoadingComponent
import com.dongsu.presentation.ui.components.LoadingComponent
import com.dongsu.presentation.ui.components.MainTopAppBar
import com.dongsu.presentation.ui.components.SaveButton
import com.dongsu.presentation.ui.overlay.content.PhotoContent
import com.dongsu.presentation.ui.overlay.content.SvgRowContent
import com.dongsu.presentation.ui.overlay.intent.EditEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun EditScreen(
    navController: NavController,
    photoUri: String?,
    albumName: String?
) {
    val editViewModel: EditViewModel = hiltViewModel()
    val state by editViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var selectPhoto: Bitmap? by remember { mutableStateOf(null) }
    var combinedBitmap: Bitmap? by remember { mutableStateOf(null) }
    var svgBitmapList: List<Bitmap>? by remember { mutableStateOf(listOf()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            editViewModel.onEvent(EditEvent.OffPermissionDialog)
        } else {
            editViewModel.onEvent(EditEvent.PermissionDenied)
        }
    }

    SideEffect {
        Timber.e("로딩: ${state.isLoading}/ 압축로딩: ${state.isLoadingCompressing}/ 저장로딩: ${state.isLoadingSaving}/ 저장 : ${state.isPhotoSaved}/ 권한 : ${state.showPermissionDeniedDialog}")
        Timber.e("다시그려짐")
    }

    LaunchedEffect(Unit) {
        editViewModel.onEvent(EditEvent.LoadSvg)
        editViewModel.onEvent(EditEvent.OffPermissionDialog)
    }

    LaunchedEffect(Unit) {
        photoUri?.let {
            selectPhoto = stringToBitmap(it)
        }
    }


    LaunchedEffect(state.svgList) {
        withContext(Dispatchers.IO) {
            val svgBitmaps = state.svgList.mapNotNull { asset ->
                convertSvgToBitmap(context, asset.content)
            }
            svgBitmapList = svgBitmaps
        }
    }

    when {
        state.isLoading -> LoadingComponent()
        state.isPhotoSaved -> {
            Timber.e("저장성공")
            navController.popBackStack(Destination.Album.route, inclusive = false)
        }
        state.isLoadingCompressing -> { GlobalLoadingComponent(message = stringResource(R.string.compressing)) }
        state.photoToCompressed != null -> { //압축 완료되면 저장 시작
            editViewModel.onEvent(EditEvent.SavePhoto(photo = state.photoToCompressed!!))
        }
        state.isLoadingSaving -> { GlobalLoadingComponent(message = stringResource(R.string.saving)) }
        state.showPermissionDeniedDialog -> {
            CommonDialog(
                dialogTitle = stringResource(R.string.denied_write_permission_title_dialog),
                dialogText = stringResource(R.string.denied_write_permission_text_dialog),
                confirmButtonText = stringResource(R.string.granted),
                onConfirmClick = {
                    openSettings(context)
                },
                onCancelClick = {
                    (context as? Activity)?.finish()
                }
            )
        }
        state.error != null -> ErrorComponent(error = state.error)
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = albumName.toString(),
                showBackButton = true,
                navController = navController,
                saveButton = {
                    SaveButton(
                        onClick = {
                            combinedBitmap?.let {
                                if (checkWritePermission(context)) {
                                    editViewModel.onEvent(EditEvent.CompressFile(it)) //저장하기 전 압축 시작
                                } else {
                                    launcher.launch(getWritePermission())
                                }
                            }
                        }
                    )
                },
                shadow = true,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PhotoContent( //Main 사진
                photo = selectPhoto,
                combinedPhoto = combinedBitmap,
                modifier = Modifier.weight(3f)
            )
            SvgRowContent( //SVG File List
                svgBitmapList,
                onClick = { selectSvg ->
                    selectPhoto?.let {
                        combinedBitmap = combineImages(it, selectSvg)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
            )
        }
    }
}
