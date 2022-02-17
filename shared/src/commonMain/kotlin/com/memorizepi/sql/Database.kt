package com.memorizepi.sql

import com.memorizepi.models.Round
import com.memorizepi.generated.AppDatabase
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class Database(driver: SqlDriver) {
    private val appDatabase = AppDatabase(driver,
        RoundAdapter = com.memorizepi.generated.Round.Adapter(
            constantAdapter = EnumColumnAdapter()
        ))

    private val queries = appDatabase.roundQueries

    fun saveRound(round: Round) {
        queries.insert(round.score, round.timestamp, round.secondsInRound, round.constant)
    }

    fun clearDB() {
        queries.clearDB()
    }

    val rounds
        get() = queries.selectAll().asFlow().mapToList()

    val scores
        get() = queries.selectAllScores().asFlow().mapToList()

    val topScore
        get() = queries.getTopScore().executeAsOne().MAX?.toInt() ?: 0

    fun topScores(count: Long) = queries.selectTopScores(count).asFlow().mapToList()
}
