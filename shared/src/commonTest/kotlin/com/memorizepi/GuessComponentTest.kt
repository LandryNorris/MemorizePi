package com.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.memorizepi.components.GuessComponent
import com.memorizepi.components.GuessState
import com.memorizepi.models.Round
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.test.*

object DefaultRoundRepository: RoundRepository {
    override fun saveGame(state: GuessState) {}
    override fun clear() {}

    override val rounds = MutableSharedFlow<List<Round>>()
    override fun topScore(constant: AppSettings.Constant): Int = 0
}

class GuessComponentTest {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @Test
    fun testInitialState() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        val expectedState = GuessState(
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0
        )
        assertEquals(expectedState, component.state.value)
    }

    @Test
    fun testGuessing() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        var expectedState = GuessState(
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0
        )
        assertEquals(expectedState, component.state.value)

        component.guessDigit('3')
        expectedState = expectedState.copy(currentScore = 1,
            startTime = component.state.value.startTime, bestScore = 1)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('1')
        expectedState = expectedState.copy(currentScore = 2,
            startTime = component.state.value.startTime, bestScore = 2)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('4')
        expectedState = expectedState.copy(currentScore = 3,
            startTime = component.state.value.startTime, bestScore = 3)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('2')
        expectedState = expectedState.copy(currentScore = 3, numIncorrect = 1,
            startTime = component.state.value.startTime, bestScore = 3)
        assertEquals(expectedState, component.state.value)
    }

    @Test
    fun testDigitHelpers() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        assertEquals('3', component.state.value.currentDigit)
        assertEquals(null, component.state.value.lastDigit(0))
        assertEquals(null, component.state.value.lastDigit(1))

        component.guessDigit('3')
        assertEquals('1', component.state.value.currentDigit)
        assertEquals('3', component.state.value.lastDigit(0))
        assertEquals(null, component.state.value.lastDigit(1))

        component.guessDigit('1')
        assertEquals('4', component.state.value.currentDigit)
        assertEquals('1', component.state.value.lastDigit(0))
        assertEquals('3', component.state.value.lastDigit(1))

        component.guessDigit('4')
        assertEquals('1', component.state.value.currentDigit)
        assertEquals('4', component.state.value.lastDigit(0))
        assertEquals('1', component.state.value.lastDigit(1))
    }

    @Test
    fun testIncorrectGuess() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        var expectedState = GuessState(
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0,
            startTime = component.state.value.startTime
        )
        assertEquals(expectedState, component.state.value)

        component.guessDigit('2')
        expectedState = expectedState.copy(numIncorrect = 1,
            startTime = component.state.value.startTime)
        assertEquals(expectedState, component.state.value)
        assertEquals('3', component.state.value.currentDigit)

        component.guessDigit('7')
        expectedState = expectedState.copy(numIncorrect = 2,
            startTime = component.state.value.startTime)
        assertEquals(expectedState, component.state.value)
        assertEquals('3', component.state.value.currentDigit)
    }

    @Test
    fun testGameOver() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        component.guessDigit('8')
        component.guessDigit('4')
        component.guessDigit('2')

        assertTrue(component.state.value.gameOver)
    }

    @Test
    fun testGuessNonDigit() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        val exception = try {
            component.guessDigit('a')
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        assertNotNull(exception)
    }

    @Test
    fun testReturnToMenu() {
        var returnedToMenu = false
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {
            returnedToMenu = true
        }

        component.returnToMenu()

        assertTrue(returnedToMenu)
    }

    @Test
    fun testRetry() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        val initialState = component.state.value
        component.guessDigit('8')
        component.guessDigit('4')
        component.guessDigit('2')

        assertTrue(component.state.value.gameOver)

        component.retry()
        assertFalse(component.state.value.gameOver)

        assertEquals(initialState, component.state.value)
    }

    @Test
    fun testHighScore() {
        val component = GuessComponent(context, AppSettings.Constant.PI, DefaultRoundRepository) {}

        assertEquals(0, component.state.value.bestScore)
    }
}
