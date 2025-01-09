package com.dongsu.presentation.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dongsu.presentation.R

@Composable
fun SaveButton(
    onClick:() -> Unit
) {
    Button(
        onClick = onClick,
    ) {
        Text(text = stringResource(R.string.save))
    }
}