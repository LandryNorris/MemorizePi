package com.memorizepi.repositories.rounds

import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import com.memorizepi.repositories.AppSettings
import kotlinx.coroutines.flow.Flow

interface RoundRepository {
    fun saveGame(state: GuessState)
    fun clear()
    fun topScore(constant: AppSettings.Constant): Int
    val rounds: Flow<List<Round>>
}
