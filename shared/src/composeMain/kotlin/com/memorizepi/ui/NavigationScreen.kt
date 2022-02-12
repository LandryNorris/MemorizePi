package com.memorizepi.ui

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.example.memorizepi.components.Navigation
import com.example.memorizepi.components.NavigationComponent
import com.memorizepi.theme.AppTheme

@Composable
fun NavigationScreen(navigationComponent: Navigation) {
    AppTheme {
        Surface {
            Children(navigationComponent.routerState,
                animation = crossfade()) {
                when(val child = it.instance) {
                    is Navigation.Child.Menu -> MenuScreen(child.component)
                    is Navigation.Child.Guess -> GuessScreen(child.component)
                    is Navigation.Child.History -> HistoryScreen(child.component)
                }
            }
        }
    }
}