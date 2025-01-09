package com.dongsu.presentation.ui.albumlist.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dongsu.domain.model.Album
import com.dongsu.presentation.R
import com.dongsu.presentation.ui.components.MainTopAppBar

@Composable
fun AlbumScreenContent(
    albumList: List<Album>,
    navController: NavController
) {
    val tabTitles = listOf(stringResource(R.string.my_album), stringResource(R.string.empty_string))
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            MainTopAppBar(
                navController = navController,
                title = stringResource(R.string.album_list)

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabContent(
                tabTitles = tabTitles,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
            when (selectedTabIndex) {
                0 -> {
                    AlbumGrid(albumList = albumList, navController = navController)
                }
                1 -> {
                    UnderConstructionScreen()
                }
            }
        }
    }
}