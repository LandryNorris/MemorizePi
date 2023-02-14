package com.memorizepi.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Theme.colors,
        typography = Theme.typography,
        content = content
    )
}

internal object Theme {
    internal val colors: Colors
        @Composable
        get() = if(isSystemInDarkTheme()) darkColors()
                else lightColors()

    internal val typography: Typography
        get() = Typography(
            defaultFontFamily = FontFamily.SansSerif
        )
}