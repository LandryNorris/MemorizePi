package com.memorizepi.ui

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.memorizepi.components.Navigation
import com.memorizepi.theme.AppTheme

@Composable
internal fun NavigationScreen(navigationComponent: Navigation) {
    AppTheme {
        Surface {
            Children(navigationComponent.childStack) {
                when(val child = it.instance) {
                    is Navigation.Child.Menu -> MenuScreen(child.component)
                    is Navigation.Child.Guess -> GuessScreen(child.component)
                    is Navigation.Child.History -> HistoryScreen(child.component)
                    is Navigation.Child.Settings -> SettingsScreen(child.component)
                }
            }
        }
    }
}