package com.memorizepi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.memorizepi.components.HistoryLogic
import com.example.memorizepi.components.HistoryState
import com.example.memorizepi.components.SortMethod
import com.example.memorizepi.models.Round

@Composable
fun HistoryScreen(logic: HistoryLogic) {
    val state = logic.state.subscribeAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(items = state.value.rounds) { round ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(round.dateString,
                            fontSize = 20.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(round.score.toString(),
                            fontSize = 40.sp)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HistoryPreview() {
    HistoryScreen(logic = object : HistoryLogic {
        override val state = MutableValue(
            HistoryState(
                rounds = listOf(
                    Round(10L, 100, 1600000000000, 30L),
                    Round(10L, 7, 1600000002000, 30L),
                    Round(10L, 50, 1600007392010, 30L),
                    Round(10L, 25, 1607390000638, 30L),
                )
            )
        )

        override fun setSortMethod(sortMethod: SortMethod) {}
    })
}