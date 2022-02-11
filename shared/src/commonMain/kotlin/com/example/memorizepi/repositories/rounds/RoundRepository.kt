package com.example.memorizepi.repositories.rounds

import com.example.memorizepi.components.GuessState
import com.example.memorizepi.models.Round
import kotlinx.coroutines.flow.Flow

abstract class RoundRepository {
    abstract fun saveGame(state: GuessState)
    abstract fun clear()
    abstract val rounds: Flow<List<Round>>
    abstract val topScore: Int
}