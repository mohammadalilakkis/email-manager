package com.example.ema.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OpenDialog(
    content: @Composable (() -> Unit),
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = "Add new contact") },
        text = content,
        confirmButton = {
            Button(onClick = onConfirm, shape = RoundedCornerShape(10.dp)) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCancel, shape = RoundedCornerShape(10.dp)) {
                Text(text = cancelText)
            }
        }
    )
}