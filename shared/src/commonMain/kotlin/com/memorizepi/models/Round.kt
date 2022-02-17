package com.memorizepi.models

import com.memorizepi.generated.Round
import com.memorizepi.repositories.AppSettings
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Round(val id: Long, val score: Int, val timestamp: Long, val secondsInRound: Long,
                 val constant: AppSettings.Constant) {
    val dateString
        get() = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault()).convertToMonthWeekDay()
}

fun LocalDateTime.convertToMonthWeekDay(): String {
    return "$dayOfMonth $month $year"
}

fun Round.toModel() = com.memorizepi.models.Round(id, score, timestamp, secondsInGame,
    constant ?: AppSettings.Constant.PI)
