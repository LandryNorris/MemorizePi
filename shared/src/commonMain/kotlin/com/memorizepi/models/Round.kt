package com.memorizepi.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Round(val id: Long, val score: Int, val timestamp: Long, val secondsInRound: Long) {
    val dateString
        get() = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault()).convertToMonthWeekDay()
}

fun LocalDateTime.convertToMonthWeekDay(): String {
    return "$dayOfMonth $month $year"
}

fun com.memorizepi.Round.toModel() = Round(id, score, timestamp, secondsInGame)