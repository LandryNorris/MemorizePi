package com.memorizepi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

@Composable
internal expect fun Popup(popupPositionProvider: PopupPositionProvider,
                          onDismissRequest: () -> Unit,
                          content: @Composable () -> Unit)

