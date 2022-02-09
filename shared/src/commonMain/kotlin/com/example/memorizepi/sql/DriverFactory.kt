package com.example.memorizepi.sql

import com.squareup.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}