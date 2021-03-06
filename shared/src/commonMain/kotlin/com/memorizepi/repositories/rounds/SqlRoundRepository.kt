package com.memorizepi.repositories.rounds

import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import com.memorizepi.models.toModel
import com.memorizepi.repositories.AppSettings
import com.memorizepi.sql.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SqlRoundRepository(private val database: Database): RoundRepository {
    override fun saveGame(state: GuessState) {
        val now = Clock.System.now()
        val secondsInGame = (now - Instant.fromEpochMilliseconds(state.startTime)).inWholeSeconds
        database.saveRound(Round(-1L, state.currentScore, state.startTime, secondsInGame,
            state.constant))
    }

    override fun clear() {
        database.clearDB()
    }

    override val rounds: Flow<List<Round>> = database.rounds.map { rounds ->
        rounds.map { it.toModel() }
    }

    fun scores(constant: AppSettings.Constant): Flow<List<Int>> = database.scores(constant)

    fun topScores(constant: AppSettings.Constant, count: Int) =
        database.topScores(constant, count.toLong())

    override fun topScore(constant: AppSettings.Constant) = database.topScore(constant)
}
