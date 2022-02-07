package com.memorizepi.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Theme.colors,
        typography = Theme.typography,
        content = content
    )
}

object Theme {
    val colors: Colors
        @Composable
        get() = if(isSystemInDarkTheme()) darkColors()
                else lightColors()

    val typography: Typography
        get() = Typography(
            defaultFontFamily = FontFamily.SansSerif
        )
}