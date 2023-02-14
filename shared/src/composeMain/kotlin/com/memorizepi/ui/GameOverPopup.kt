package com.memorizepi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun GameOverPopup(returnToMenu: () -> Unit = {}, retry: () -> Unit = {}) {
    Popup(onDismissRequest = {}, popupPositionProvider = positionProvider()) {
        Column(Modifier.background(MaterialTheme.colors.surface).size(150.dp, 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            Text("Game Over", color = MaterialTheme.colors.onSurface)
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(content = { Text("Menu") },
                    onClick = returnToMenu)
                Button(content = { Text("Retry") },
                    onClick = retry)
            }
        }
    }
}
