package com.example.memorizepi.sql

import com.example.memorizepi.models.Round
import com.memorizepi.AppDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class Database(factory: DriverFactory) {
    private val database = AppDatabase(factory.createDriver())
    private val queries = database.roundQueries

    fun saveRound(round: Round) {
        queries.insert(round.score, round.timestamp, round.secondsInRound)
    }

    val rounds = queries.selectAll().asFlow().mapToList()

    val scores = queries.selectAllScores().asFlow().mapToList()

    fun topScores(count: Long) = queries.selectTopScores(count).asFlow().mapToList()
}