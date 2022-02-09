package com.example.memorizepi.repositories.rounds

import com.example.memorizepi.components.GuessState
import com.example.memorizepi.models.Round
import com.example.memorizepi.models.toModel
import com.example.memorizepi.sql.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SqlRoundRepository(private val database: Database): RoundRepository() {
    override fun saveGame(state: GuessState) {
        val now = Clock.System.now()
        val secondsInGame = (now - Instant.fromEpochMilliseconds(state.startTime)).inWholeSeconds
        database.saveRound(Round(-1L, state.currentScore, now.toEpochMilliseconds(), secondsInGame))
    }

    override val rounds: Flow<List<Round>> = database.rounds.map {
            rounds -> rounds.map { it.toModel() }
    }
}