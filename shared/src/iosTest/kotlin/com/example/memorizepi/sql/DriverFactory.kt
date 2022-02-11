package com.example.memorizepi.sql

import co.touchlab.sqliter.DatabaseConfiguration
import com.memorizepi.generated.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val schema = AppDatabase.Schema
        return NativeSqliteDriver(
            DatabaseConfiguration(
                "library.db",
                version = schema.version,
                create = { connection ->
                    wrapConnection(connection) { schema.create(it) }
                },
                inMemory = true
            )
        )
    }
}