package com.memorizepi.sql

import com.memorizepi.models.Round
import com.memorizepi.generated.AppDatabase
import com.memorizepi.repositories.AppSettings
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

    fun scores(constant: AppSettings.Constant) = queries.selectScores(constant).asFlow().mapToList()

    fun topScore(constant: AppSettings.Constant) =
        queries.getTopScore(constant).executeAsOne().MAX?.toInt() ?: 0

    fun topScores(constant: AppSettings.Constant, count: Long) =
        queries.selectTopScores(constant, count).asFlow().mapToList()
}
