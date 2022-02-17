package com.memorizepi.repositories.rounds

import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import kotlinx.coroutines.flow.Flow

interface RoundRepository {
    fun saveGame(state: GuessState)
    fun clear()
    val rounds: Flow<List<Round>>
    val topScore: Int
}
