package com.memorizepi

import android.content.Context
import com.memorizepi.repositories.rounds.RoundRepository
import com.memorizepi.repositories.rounds.SqlRoundRepository
import com.memorizepi.sql.Database
import com.memorizepi.sql.DriverFactory
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

fun initialize(context: Context) {
    val driverFactory = DriverFactory(context.applicationContext).createDriver()
    val koinModule = module {
        single { Database(driverFactory) }
        single<RoundRepository> { SqlRoundRepository(get()) }
    }

    startKoin {
        modules(koinModule)
    }
}