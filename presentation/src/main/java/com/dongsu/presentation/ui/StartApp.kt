package com.dongsu.presentation.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongsu.presentation.R
import com.dongsu.presentation.common.CommonDialog
import com.dongsu.presentation.common.checkMemoryUsage
import com.dongsu.presentation.common.getGalleryPermission
import com.dongsu.presentation.common.openSettings
import com.dongsu.presentation.navigation.AppNavigation
import timber.log.Timber

@Composable
fun StartApp() {
    val context = LocalContext.current
    val startAppViewModel: StartAppViewModel = hiltViewModel()
    val state by startAppViewModel.uiState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startAppViewModel.onEvent(StartAppEvent.PermissionGranted)
        } else {
            startAppViewModel.onEvent(StartAppEvent.PermissionDenied)
        }
    }

    SideEffect {
        checkMemoryUsage()
        Timber.e("다시그려짐")
    }

    LaunchedEffect(Unit) {
        startAppViewModel.onEvent(StartAppEvent.CheckReadPermission(context))
    }

    LifecycleResumeEffect(state.isReadPermissionGranted) {
        Timber.e("onResume or 초기 권한 확인 : ${state.isReadPermissionGranted}")
        onPauseOrDispose {
            Timber.e("onPause or 권한 Granted : ${state.isReadPermissionGranted}")
        }
    }

    when {
        state.isReadPermissionGranted -> AppNavigation()
        state.isReadPermissionNotGranted -> launcher.launch(getGalleryPermission())
        state.showPermissionDeniedDialog -> {
            CommonDialog(
                dialogTitle = stringResource(R.string.denied_read_permission_title_dialog),
                dialogText = stringResource(R.string.denied_read_permission_text_dialog),
                confirmButtonText = stringResource(R.string.granted),
                cancelButtonText = stringResource(R.string.finish),
                onConfirmClick = {
                    openSettings(context)
                    startAppViewModel.onEvent(StartAppEvent.RetryCheckReadPermission(context))
                },
                onCancelClick = {
                    (context as? Activity)?.finish()
                }
            )
        }

        state.shouldShowRationale -> launcher.launch(getGalleryPermission())
    }
}



//     Runtime Permission체크 - ViewModel, Event, State 사용 X

//   var isReadPermissionGranted by remember { mutableStateOf(false) }
//   var shouldShowPermissionDialog by remember { mutableStateOf(false) }
//
//   val launcher = rememberLauncherForActivityResult(
//      contract = ActivityResultContracts.RequestPermission()
//   ) { isGranted ->
//      if (isGranted) {
//         isReadPermissionGranted = true
//         shouldShowPermissionDialog = false
//      } else {
//         shouldShowPermissionDialog = true
//      }
//   }
//
//   LaunchedEffect(Unit) {
//      isReadPermissionGranted = checkReadImagePermission(context)
//      if (!isReadPermissionGranted) {
//         launcher.launch(getGalleryPermission())
//         isReadPermissionGranted = checkReadImagePermission(context)
//      }
//   }
//
//   if (shouldShowPermissionDialog) {
//      CommonDialog(
//         dialogTitle = stringResource(R.string.denied_read_permission_title_dialog),
//         dialogText = stringResource(R.string.denied_read_permission_text_dialog),
//         onConfirmClick = {
//            isReadPermissionGranted = shouldShowRequestPermissionRationale(context, arrayOf( getGalleryPermission()))
//            if (isReadPermissionGranted) {
//               launcher.launch(getGalleryPermission())
//               isReadPermissionGranted = checkReadImagePermission(context)
//            }
//            shouldShowPermissionDialog = false
//         },
//         onCancelClick = {
//            (context as? Activity)?.finish() // 앱 종료
//         }
//      )
//   }
//
//   if (isReadPermissionGranted) {
//      AppNavigation()
//   }
//}
