package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessState
import com.example.memorizepi.components.HistoryComponent
import com.example.memorizepi.components.HistoryState
import com.example.memorizepi.components.SortMethod
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.SqlRoundRepository
import com.example.memorizepi.sql.Database
import com.example.memorizepi.sql.DriverFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HistoryTests {
    private val context = DefaultComponentContext(LifecycleRegistry())
    private val driver
        get() = DriverFactory().createDriver().also {
            SqlRoundRepository(Database(it)).clear() //Clear between test runs
        }

    @Test
    fun testDefaultState() {
        val historyState = HistoryState()

        assertTrue(historyState.rounds.isEmpty())
        assertEquals(SortMethod.NEWEST, historyState.sortMethod)

        val rounds = listOf(Round(1L, 14, 183947L, 12L))
        val state2 = HistoryState(rounds = rounds)
        assertTrue(state2.rounds.isNotEmpty())
    }

    @Test
    fun testGetRoundHistoryNewest() = runBlocking {
        val repo = SqlRoundRepository(Database((driver)))
        val historyComponent = HistoryComponent(context, repo).also {
            it.setSortMethod(SortMethod.NEWEST)
        }
        insertTestData(repo)

        val rounds = historyComponent.rounds.first()

        assertEquals(6, rounds.size)
        val scores = listOf(50, 100, 15, 7, 15, 17)

        rounds.zip(scores).forEach {
            val round = it.first
            val score = it.second
            assertEquals(score, round.score)
        }
    }

    @Test
    fun testGetRoundHistoryOldest() = runBlocking {
        val repo = SqlRoundRepository(Database((driver)))
        val historyComponent = HistoryComponent(context, repo).also {
            it.setSortMethod(SortMethod.OLDEST)
        }
        insertTestData(repo)

        val rounds = historyComponent.rounds.first()

        assertEquals(6, rounds.size)
        val scores = listOf(17, 15, 7, 15, 100, 50)

        rounds.zip(scores).forEach {
            val round = it.first
            val score = it.second
            assertEquals(score, round.score)
        }
    }

    @Test
    fun testGetRoundHistoryBest() = runBlocking {
        val repo = SqlRoundRepository(Database((driver)))
        val historyComponent = HistoryComponent(context, repo).also {
            it.setSortMethod(SortMethod.BEST)
        }
        insertTestData(repo)

        val rounds = historyComponent.rounds.first()

        assertEquals(6, rounds.size)
        val scores = listOf(100, 50, 17, 15, 15, 7)

        rounds.zip(scores).forEach {
            val round = it.first
            val score = it.second
            assertEquals(score, round.score)
        }
    }

    @Test
    fun testGetRoundHistoryWorst() = runBlocking {
        val repo = SqlRoundRepository(Database((driver)))
        val historyComponent = HistoryComponent(context, repo).also {
            it.setSortMethod(SortMethod.WORST)
        }
        insertTestData(repo)

        val rounds = historyComponent.rounds.first()

        assertEquals(6, rounds.size)
        val scores = listOf(7, 15, 15, 17, 50, 100)

        rounds.zip(scores).forEach {
            val round = it.first
            val score = it.second
            assertEquals(score, round.score)
        }
    }

    private fun insertTestData(repo: SqlRoundRepository) {
        repo.saveGame(GuessState(digits = "1289374", startTime = 78929473L, currentScore = 15))
        repo.saveGame(GuessState(digits = "1289374", startTime = 98290302L, currentScore = 50))
        repo.saveGame(GuessState(digits = "1289374", startTime = 75832902L, currentScore = 7))
        repo.saveGame(GuessState(digits = "1289374", startTime = 91038853L, currentScore = 100))
        repo.saveGame(GuessState(digits = "1289374", startTime = 10294458L, currentScore = 17))
        repo.saveGame(GuessState(digits = "1289374", startTime = 28910384L, currentScore = 15))
    }
}