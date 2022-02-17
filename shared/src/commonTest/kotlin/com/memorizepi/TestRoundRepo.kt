package com.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.memorizepi.components.GuessComponent
import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestRoundRepo {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @Test
    fun testStartingBestScore() {
        val repo = object: RoundRepository {
            override fun saveGame(state: GuessState) {}
            override fun clear() {}

            override val rounds = MutableSharedFlow<List<Round>>()
            override fun topScore(constant: AppSettings.Constant): Int = 5
        }

        val component = GuessComponent(context, AppSettings.Constant.PI, repo) {}

        assertEquals(5, component.state.value.bestScore)
    }

    @Test
    fun testBestScoreIncreasesAsYouBeatIt() {
        val repo = object: RoundRepository {
            override fun saveGame(state: GuessState) {}
            override fun clear() {}

            override val rounds = MutableSharedFlow<List<Round>>()
            override fun topScore(constant: AppSettings.Constant): Int = 3
        }

        val component = GuessComponent(context, AppSettings.Constant.PI, repo) {}

        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('3')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('1')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('4')
        assertEquals(3, component.state.value.bestScore)

        component.guessDigit('1')
        assertEquals(4, component.state.value.bestScore)

        component.guessDigit('5')
        assertEquals(5, component.state.value.bestScore)

        //wrong guess
        component.guessDigit('0')
        assertEquals(5, component.state.value.bestScore)

        component.guessDigit('9')
        assertEquals(6, component.state.value.bestScore)
    }

    @Test
    fun testSaveGameCalled() {
        var saveGameCalled = false
        val repo = object: RoundRepository {
            override fun saveGame(state: GuessState) {
                saveGameCalled = true
            }
            override fun clear() {}

            override val rounds = MutableSharedFlow<List<Round>>()
            override fun topScore(constant: AppSettings.Constant): Int = 3
        }

        val component = GuessComponent(context, AppSettings.Constant.PI, repo) {}

        component.guessDigit('3')
        assertFalse(saveGameCalled)

        component.guessDigit('1')
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