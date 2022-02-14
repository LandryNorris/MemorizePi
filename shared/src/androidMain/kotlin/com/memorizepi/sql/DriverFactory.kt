package com.memorizepi.sql

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.memorizepi.generated.AppDatabase

actual open class DriverFactory(private val context: Context?) {
    actual open fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context!!, "memorizepi.db")
    }
}