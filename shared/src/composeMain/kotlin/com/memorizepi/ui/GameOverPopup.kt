package com.memorizepi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameOverPopup(returnToMenu: () -> Unit = {}, retry: () -> Unit = {}) {
    AlertDialog(onDismissRequest = {},
        title = {
            Text("Game Over")
        },
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(content = { Text("Menu") },
                    onClick = returnToMenu)
                Button(content = { Text("Retry") },
                    onClick = retry)
            }
        }
    )
}

@Preview
@Composable
fun GameOverPopupPreview() {
    GameOverPopup()
}