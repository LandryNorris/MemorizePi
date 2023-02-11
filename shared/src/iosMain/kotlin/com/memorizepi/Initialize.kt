package com.memorizepi

import com.memorizepi.repositories.rounds.RoundRepository
import com.memorizepi.repositories.rounds.SqlRoundRepository
import com.memorizepi.sql.Database
import com.memorizepi.sql.DriverFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initialize() {
    val driverFactory = DriverFactory().createDriver()
    val koinModule = module {
        single { Database(driverFactory) }
        single<RoundRepository> { SqlRoundRepository(get()) }
    }

    startKoin {
        modules(koinModule)
    }
}
