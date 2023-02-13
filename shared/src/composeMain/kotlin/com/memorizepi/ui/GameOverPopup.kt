package com.memorizepi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
internal fun GameOverPopup(returnToMenu: () -> Unit = {}, retry: () -> Unit = {}) {
    Popup(onDismissRequest = {}, popupPositionProvider = positionProvider()) {
        Column {
            Text("Game Over")

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(content = { Text("Menu") },
                    onClick = returnToMenu)
                Button(content = { Text("Retry") },
                    onClick = retry)
            }
        }
    }
}
