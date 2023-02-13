package com.memorizepi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.memorizepi.components.MenuLogic

@Composable
internal fun MenuScreen(menuComponent: MenuLogic) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            content = { Text("Guess") },
            onClick = menuComponent::goToGuess
        )
        Button(
            content = { Text("History") },
            onClick = menuComponent::goToHistory
        )
        Button(
            content = { Text("Settings") },
            onClick = menuComponent::goToSettings
        )
    }
}