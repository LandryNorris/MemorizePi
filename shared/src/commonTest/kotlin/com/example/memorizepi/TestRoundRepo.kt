package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessComponent
import com.example.memorizepi.components.GuessState
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestRoundRepo {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @Test
    fun testStartingBestScore() {
        val repo = object: RoundRepository() {
            override fun saveGame(state: GuessState) {
            }

            override val rounds = MutableSharedFlow<List<Round>>()
            override val topScore: Int = 5
        }

        val digits = "123456"
        val component = GuessComponent(context, digits, repo) {}

        assertEquals(5, component.state.value.bestScore)
    }

    @Test
    fun testBestScoreIncreasesAsYouBeatIt() {
        val repo = object: RoundRepository() {
            override fun saveGame(state: GuessState) {
            }

            override val rounds = MutableSharedFlow<List<Round>>()
            override val topScore: Int = 3
        }

        val digits = "12345678"
        val component = GuessComponent(context, digits, repo) {}

        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('1')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('2')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('3')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('4')
        assertEquals(4, component.state.value.bestScore)

        component.guessDigit('5')
        assertEquals(5, component.state.value.bestScore)

        //wrong guess
        component.guessDigit('0')
        assertEquals(5, component.state.value.bestScore)

        component.guessDigit('6')
        assertEquals(6, component.state.value.bestScore)
    }

    @Test
    fun testSaveGameCalled() {
        var saveGameCalled = false
        val repo = object: RoundRepository() {
            override fun saveGame(state: GuessState) {
                saveGameCalled = true
            }

            override val rounds = MutableSharedFlow<List<Round>>()
            override val topScore: Int = 3
        }

        val digits = "184904532"
        val component = GuessComponent(context, digits, repo) {}

        component.guessDigit('1')
        assertFalse(saveGameCalled)

        component.guessDigit('8')
        assertFalse(saveGameCalled)

        //wrong digit
        component.guessDigit('2')
        assertFalse(saveGameCalled)

        //wrong digit
        component.guessDigit('2')
        assertFalse(saveGameCalled)

        //third wrong guess
        component.guessDigit('2')
        assertTrue(saveGameCalled)
    }
}