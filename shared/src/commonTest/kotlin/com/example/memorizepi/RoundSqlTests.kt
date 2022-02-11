package com.example.memorizepi

import com.example.memorizepi.components.GuessState
import com.example.memorizepi.repositories.rounds.SqlRoundRepository
import com.example.memorizepi.sql.Database
import com.example.memorizepi.sql.DriverFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RoundSqlTests {
    private val driver
        get() = DriverFactory().createDriver().also {
           SqlRoundRepository(Database(it)).clear() //Clear between test runs
        }

    @Test
    fun testSaveGame() = runBlocking {
        val repo = SqlRoundRepository(Database(driver))

        val startTime = 6389102334782L
        val gameState = GuessState(digits = "1289374", startTime = startTime, currentScore = 15)
        repo.saveGame(gameState)
        assertEquals(1, repo.rounds.first().size)

        repo.saveGame(gameState)
        assertEquals(2, repo.rounds.first().size)
    }

    @Test
    fun testGetBestScore() = runBlocking {
        val repo = SqlRoundRepository(Database(driver))
        assertEquals(0,  repo.topScore)

        val startTime = 772894892L
        val gameState = GuessState(digits = "1289374", startTime = startTime, currentScore = 17)
        repo.saveGame(gameState)

        assertEquals(17, repo.topScore)
    }
}