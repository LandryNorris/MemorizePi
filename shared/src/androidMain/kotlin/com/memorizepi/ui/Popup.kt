package com.memorizepi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.PopupPositionProvider

@Composable
internal actual fun Popup(popupPositionProvider: PopupPositionProvider, onDismissRequest: () -> Unit,
                          content: @Composable () -> Unit) {
    androidx.compose.ui.window.Popup(popupPositionProvider = popupPositionProvider,
        onDismissRequest = onDismissRequest,
        content = content)
}
