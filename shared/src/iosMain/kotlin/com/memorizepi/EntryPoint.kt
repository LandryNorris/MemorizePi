package com.memorizepi

import androidx.compose.ui.window.Application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.memorizepi.components.NavigationComponent
import com.memorizepi.ui.NavigationScreen
import platform.UIKit.UIViewController

@Suppress("unused") //called from Swift
object EntryPoint {
    fun createComposeViewController(): UIViewController {
        initialize()
        val context = DefaultComponentContext(LifecycleRegistry())
        val navigation = NavigationComponent(context)

        return Application {
            NavigationScreen(navigation)
        }
    }
}