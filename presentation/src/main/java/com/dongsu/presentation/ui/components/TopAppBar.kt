package com.dongsu.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dongsu.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    navController: NavController,
    title: String,
    showBackButton: Boolean = false,
    saveButton: @Composable (() -> Unit)? = null,
    shadow: Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.default_padding))
                )
        },
        modifier = Modifier.
        shadow(if (shadow) dimensionResource(R.dimen.default_padding) else 0.dp),
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_btn),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.default_padding))
                    )
                }
            }
        },
        actions = {
            saveButton?.invoke()
        },

    )
}