package com.dongsu.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun CommonDialog(
    dialogTitle: String,
    dialogText: String,
    confirmButtonText: String = "확인",
    cancelButtonText: String = "취소 ",
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = onCancelClick,
        confirmButton = {
            Button(onClick = onConfirmClick) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onCancelClick) {
                Text(text = cancelButtonText)
            }
        },
    )
}