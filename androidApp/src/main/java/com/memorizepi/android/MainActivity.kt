package com.memorizepi.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.memorizepi.components.NavigationComponent
import com.memorizepi.ui.NavigationScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navigation = NavigationComponent(defaultComponentContext())
            NavigationScreen(navigation)
        }
    }
}
