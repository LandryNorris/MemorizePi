package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessComponent
import com.example.memorizepi.components.GuessState
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProvider
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
import kotlin.jvm.JvmStatic
import kotlin.test.*

object DefaultRoundRepository: RoundRepository() {
    override fun saveGame(state: GuessState) {}

    override val rounds = MutableSharedFlow<List<Round>>()
    override val topScore: Int = 0
}

class GuessComponentTest: KoinTest {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @BeforeTest
    fun init() {
        startKoin {}
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    @Test
    fun testInitialState() {
        declareDefaultRepo()
        val digits = "75832964238291"
        val component = GuessComponent(context, digits) {}

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
        declareDefaultRepo()
        val digits = "72867429910"
        val component = GuessComponent(context, digits) {}

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
        declareDefaultRepo()
        val digits = "758294023"
        val component = GuessComponent(context, digits) {}

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
        declareDefaultRepo()
        val digits = "789423698305403"
        val component = GuessComponent(context, digits) {}

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
        declareDefaultRepo()
        val digits = "752894903821"
        val component = GuessComponent(context, digits) {}

        component.guessDigit('8')
        component.guessDigit('4')
        component.guessDigit('2')

        assertTrue(component.state.value.gameOver)
    }

    @Test
    fun testHighScore() {
        declareDefaultRepo()
        val digits = "752894903821"
        val component = GuessComponent(context, digits) {}

        assertEquals(0, component.state.value.bestScore)
    }

    private fun declareDefaultRepo() {
        declare {
            DefaultRoundRepository as RoundRepository
        }
    }
}
