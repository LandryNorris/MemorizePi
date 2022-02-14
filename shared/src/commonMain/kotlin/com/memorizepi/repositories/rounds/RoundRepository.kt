package com.memorizepi.repositories.rounds

import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import kotlinx.coroutines.flow.Flow

abstract class RoundRepository {
    abstract fun saveGame(state: GuessState)
    abstract fun clear()
    abstract val rounds: Flow<List<Round>>
    abstract val topScore: Int
}