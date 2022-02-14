package com.memorizepi

import com.memorizepi.components.GuessState
import com.memorizepi.repositories.rounds.SqlRoundRepository
import com.memorizepi.sql.Database
import com.memorizepi.sql.DriverFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun testGetScores() = runBlocking {
        val repo = SqlRoundRepository(Database(driver))
        assertEquals(0,  repo.topScore)

        val startTime = 772894892L
        val gameState = GuessState(digits = "1289374", startTime = startTime, currentScore = 17)
        repo.saveGame(gameState)

        val scores = repo.scores.first()
        assertEquals(1, scores.size)
        assertEquals(17, scores.first())
    }

    @Test
    fun testGetTopScores() = runBlocking {
        val repo = SqlRoundRepository(Database(driver))

        val startTime = 772894892L
        repo.saveGame(GuessState(digits = "1289374", startTime = startTime, currentScore = 17))

        val topScores = repo.topScores(2).first()
        assertEquals(1, topScores.size) //there is only one item

        repo.saveGame(GuessState(digits = "1289374", startTime = startTime, currentScore = 12))
        val topScores2 = repo.topScores(2).first()
        assertEquals(2, topScores2.size) //there is only one item
    }

    @Test
    fun testGeneratedCode() {
        val round = Round(0L, 109, 1938574L, 30L)
        assertTrue(round.toString().isNotEmpty())

        val topScore = GetTopScore(MAX = 100L)
        assertEquals(100L, topScore.MAX)
        assertTrue(topScore.toString().isNotEmpty())
    }
}