package com.memorizepi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.memorizepi.components.GuessLogic
import com.example.memorizepi.components.GuessState

@Composable
fun GuessScreen(component: GuessLogic) {
    val state by component.state.subscribeAsState()

    if(state.gameOver) {
        GameOverPopup(returnToMenu = component::returnToMenu,
            retry = component::retry)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom) {
            Text(modifier = Modifier.testTag("last2"),
                text = state.lastDigit(2)?.toString() ?: "_",
                fontSize = 40.sp)
            Text(modifier = Modifier.testTag("last"),
                text = state.lastDigit(1)?.toString() ?: "_",
                fontSize = 75.sp)
            Text(modifier = Modifier.testTag("current"),
                text = state.lastDigit(0)?.toString() ?: "_",
                fontSize = 40.sp)
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom) {
            Text(modifier = Modifier.testTag("wrong1"),
                text = if(state.numIncorrect > 0) "X" else " ",
                fontSize = 50.sp, color = Color.Red)
            Text(modifier = Modifier.testTag("wrong2"),
                text = if(state.numIncorrect > 1) "X" else " ",
                fontSize = 100.sp, color = Color.Red)
            Text(modifier = Modifier.testTag("wrong3"),
                text = if(state.numIncorrect > 2) "X" else " ",
                fontSize = 50.sp, color = Color.Red)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(modifier = Modifier.testTag("bestScore"),
                text = "Best score: ${state.bestScore}",
                fontSize = 20.sp)

            Spacer(Modifier.weight(1f))

            Text(modifier = Modifier.testTag("currentScore"),
                text = "Current score: ${state.currentScore}",
                fontSize = 20.sp)
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            (0 until 3).forEach { rowIndex ->
                Row(modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    (0 until 3).forEach { columnIndex ->
                        val buttonIndex = columnIndex + 3*rowIndex
                        val buttonText = (buttonIndex+1).toString()
                        Button(modifier = Modifier.height(IntrinsicSize.Max)
                            .testTag("Button$buttonText"),
                            content = { Text(buttonText) }, onClick = {
                            //each String is only one char, so we just get the first.
                            component.guessDigit(buttonText.first())
                        })
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                Button(modifier = Modifier.height(IntrinsicSize.Max)
                    .testTag("Button0"),
                    content = { Text("0") }, onClick = {
                        component.guessDigit('0')
                    })
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GuessPreview() {
    MaterialTheme {
        GuessScreen(object: GuessLogic {
            override val state = MutableValue(GuessState(currentScore = 7, numIncorrect = 2))
        })
    }
}

@Preview(showSystemUi = true)
@Composable
fun GuessPreview2() {
    MaterialTheme {
        GuessScreen(object: GuessLogic {
            override val state = MutableValue(GuessState(currentScore = 7, numIncorrect = 3, gameOver = true))
        })
    }
}