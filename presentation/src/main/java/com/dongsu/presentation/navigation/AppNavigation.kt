package com.dongsu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dongsu.presentation.common.UTF_8
import com.dongsu.presentation.ui.albumlist.AlbumScreen
import com.dongsu.presentation.ui.overlay.EditScreen
import com.dongsu.presentation.ui.photopicker.PhotosScreen
import com.dongsu.presentation.theme.PicFusionTheme
import java.net.URLDecoder

@Composable
fun AppNavigation(){
    PicFusionTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Destination.Album.route) {
            composable(Destination.Album.route) {
                AlbumScreen(navController)
            }
            composable(Destination.Photos.route + "/{albumId}/{albumName}") { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("albumId")
                val albumName = backStackEntry.arguments?.getString("albumName")
                PhotosScreen(navController, albumId, albumName)
            }
            composable(Destination.Edit.route + "/{photoUri}/{albumName}") { backStackEntry ->
                val photoUri = backStackEntry.arguments?.getString("photoUri")?.let {
                    URLDecoder.decode(it, UTF_8)
                }
                val albumName = backStackEntry.arguments?.getString("albumName")
                EditScreen(navController, photoUri, albumName)
            }
        }
    }
}

