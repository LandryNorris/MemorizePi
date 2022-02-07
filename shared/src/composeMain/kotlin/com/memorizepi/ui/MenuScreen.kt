package com.memorizepi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.memorizepi.components.MenuLogic

@Composable
fun MenuScreen(menuComponent: MenuLogic) {
    Column(verticalArrangement = Arrangement.SpaceEvenly) {
        Button(
            content = { Text("Guess") },
            onClick = menuComponent::goToGuess
        )
    }
}