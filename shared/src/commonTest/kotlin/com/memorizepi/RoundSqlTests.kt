package com.memorizepi

import com.memorizepi.components.GuessState
import com.memorizepi.generated.Round
import com.memorizepi.repositories.AppSettings
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
        val gameState = GuessState(startTime = startTime, currentScore = 15)
        repo.saveGame(gameState)
        assertEquals(1, repo.rounds.first().size)

        repo.saveGame(gameState)
        assertEquals(2, repo.rounds.first().size)
    }

    @Test
    fun testGetBestScore() = runBlocking {
        val constant = AppSettings.Constant.SQRT2
        val repo = SqlRoundRepository(Database(driver))
        assertEquals(0,  repo.topScore(constant))

        val startTime = 772894892L
        val gameState = GuessState(startTime = startTime, currentScore = 17, constant = constant)
        repo.saveGame(gameState)

        assertEquals(17, repo.topScore(constant))
    }

    @Test
    fun testGetScores() = runBlocking {
        val constant = AppSettings.Constant.E
        val repo = SqlRoundRepository(Database(driver))
        assertEquals(0,  repo.topScore(constant))

        val startTime = 772894892L
        val gameState = GuessState(startTime = startTime, currentScore = 17, constant = constant)
        repo.saveGame(gameState)

        val scores = repo.scores(constant).first()
        assertEquals(1, scores.size)
        assertEquals(17, scores.first())
    }

    @Test
    fun testConstantScoresAreIndependent() = runBlocking {
        val repo = SqlRoundRepository(Database(driver))

        val startTime = 772894892L
        repo.saveGame(GuessState(startTime = startTime, currentScore = 17,
            constant = AppSettings.Constant.E))

        val eTopScores = repo.topScores(AppSettings.Constant.E, 2).first()
        assertEquals(1, eTopScores.size) //there is only one item
        assertEquals(17, eTopScores.first())

        val piTopScores = repo.topScores(AppSettings.Constant.PI, 2).first()
        assertEquals(0, piTopScores.size) //there is only one item

        repo.saveGame(GuessState(startTime = startTime+10000, currentScore = 15,
            constant = AppSettings.Constant.SQRT2))
        val sqrt2Scores = repo.topScores(AppSettings.Constant.SQRT2, 2).first()
        assertEquals(1, sqrt2Scores.size)
        assertEquals(15, sqrt2Scores.first())
    }

    @Test
    fun testGetTopScores() = runBlocking {
        val constant = AppSettings.Constant.E
        val repo = SqlRoundRepository(Database(driver))

        val startTime = 772894892L
        repo.saveGame(GuessState(startTime = startTime, currentScore = 17, constant = constant))

        val topScores = repo.topScores(constant, 2).first()
        assertEquals(1, topScores.size) //there is only one item

        repo.saveGame(GuessState(startTime = startTime, currentScore = 12, constant = constant))
        val topScores2 = repo.topScores(constant, 2).first()
        assertEquals(2, topScores2.size) //there is only one item
    }

    @Test
    fun testGeneratedCode() {
        val round = Round(0L, 109, 1938574L, 30L,
            AppSettings.Constant.PI)
        assertTrue(round.toString().isNotEmpty())

        val topScore = GetTopScore(MAX = 100L)
        assertEquals(100L, topScore.MAX)
        assertTrue(topScore.toString().isNotEmpty())
    }
}