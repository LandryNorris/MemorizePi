package com.example.memorizepi.models

data class Round(val id: Long, val score: Int, val timestamp: Long, val secondsInRound: Long) {
    fun toSqlModel() = com.memorizepi.Round(id, score, timestamp, secondsInRound)
}

fun com.memorizepi.Round.toModel() = Round(id, score, timestamp, secondsInGame)