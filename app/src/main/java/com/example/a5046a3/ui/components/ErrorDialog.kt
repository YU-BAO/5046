package com.example.a5046a3.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A simple error dialog to display error messages to the user
 *
 * @param title Dialog title
 * @param message Error message to display
 * @param onDismiss Callback when dialog is dismissed
 * @param modifier Modifier for the dialog
 */
@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        modifier = modifier
    )
}

