package com.memorizepi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.memorizepi.components.HistoryLogic
import com.memorizepi.components.HistoryState

@Composable
fun HistoryScreen(logic: HistoryLogic) {
    val state by logic.state.collectAsState(initial = HistoryState())
    val rounds by logic.rounds.collectAsState(initial = listOf())
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(items = rounds) { round ->
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
