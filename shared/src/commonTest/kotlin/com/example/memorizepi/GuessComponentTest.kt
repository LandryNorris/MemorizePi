package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessComponent
import com.example.memorizepi.components.GuessState
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.*

object DefaultRoundRepository: RoundRepository() {
    override fun saveGame(state: GuessState) {}

    override val rounds = MutableSharedFlow<List<Round>>()
    override val topScore: Int = 0
}

class GuessComponentTest {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @Test
    fun testInitialState() {
        val digits = "75832964238291"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        val expectedState = GuessState(
            digits = digits,
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0
        )
        assertEquals(expectedState, component.state.value)
    }

    @Test
    fun testGuessing() {
        val digits = "72867429910"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        var expectedState = GuessState(
            digits = digits,
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0
        )
        assertEquals(expectedState, component.state.value)

        component.guessDigit('7')
        expectedState = expectedState.copy(currentScore = 1,
            startTime = component.state.value.startTime, bestScore = 1)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('2')
        expectedState = expectedState.copy(currentScore = 2,
            startTime = component.state.value.startTime, bestScore = 2)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('8')
        expectedState = expectedState.copy(currentScore = 3,
            startTime = component.state.value.startTime, bestScore = 3)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('0')
        expectedState = expectedState.copy(currentScore = 3, numIncorrect = 1,
            startTime = component.state.value.startTime, bestScore = 3)
        assertEquals(expectedState, component.state.value)
    }

    @Test
    fun testDigitHelpers() {
        val digits = "758294023"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        assertEquals('7', component.state.value.currentDigit)
        assertEquals(null, component.state.value.lastDigit(0))
        assertEquals(null, component.state.value.lastDigit(1))

        component.guessDigit('7')
        assertEquals('5', component.state.value.currentDigit)
        assertEquals('7', component.state.value.lastDigit(0))
        assertEquals(null, component.state.value.lastDigit(1))

        component.guessDigit('5')
        assertEquals('8', component.state.value.currentDigit)
        assertEquals('5', component.state.value.lastDigit(0))
        assertEquals('7', component.state.value.lastDigit(1))

        component.guessDigit('8')
        assertEquals('2', component.state.value.currentDigit)
        assertEquals('8', component.state.value.lastDigit(0))
        assertEquals('5', component.state.value.lastDigit(1))
    }

    @Test
    fun testIncorrectGuess() {
        val digits = "789423698305403"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        var expectedState = GuessState(
            digits = digits,
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0,
            startTime = component.state.value.startTime
        )
        assertEquals(expectedState, component.state.value)

        component.guessDigit('3')
        expectedState = expectedState.copy(numIncorrect = 1,
            startTime = component.state.value.startTime)
        assertEquals(expectedState, component.state.value)
        assertEquals('7', component.state.value.currentDigit)

        component.guessDigit('3')
        expectedState = expectedState.copy(numIncorrect = 2,
            startTime = component.state.value.startTime)
        assertEquals(expectedState, component.state.value)
        assertEquals('7', component.state.value.currentDigit)
    }

    @Test
    fun testGameOver() {
        val digits = "752894903821"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        component.guessDigit('8')
        component.guessDigit('4')
        component.guessDigit('2')

        assertTrue(component.state.value.gameOver)
    }

    @Test
    fun testHighScore() {
        val digits = "752894903821"
        val component = GuessComponent(context, digits, DefaultRoundRepository) {}

        assertEquals(0, component.state.value.bestScore)
    }
}
