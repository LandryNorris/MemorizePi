package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessComponent
import com.example.memorizepi.components.GuessState
import kotlin.test.Test
import kotlin.test.assertEquals

class GuessComponentTest {
    private val context = DefaultComponentContext(LifecycleRegistry())

    @Test
    fun testInitialState() {
        val digits = "75832964238291"
        val component = GuessComponent(context, digits)

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
        val component = GuessComponent(context, digits)

        var expectedState = GuessState(
            digits = digits,
            currentScore = 0,
            bestScore = 0,
            numIncorrect = 0
        )
        assertEquals(expectedState, component.state.value)

        component.guessDigit('7')
        expectedState = expectedState.copy(currentScore = 1)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('2')
        expectedState = expectedState.copy(currentScore = 2)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('8')
        expectedState = expectedState.copy(currentScore = 3)
        assertEquals(expectedState, component.state.value)

        component.guessDigit('0')
        expectedState = expectedState.copy(currentScore = 3, numIncorrect = 1)
        assertEquals(expectedState, component.state.value)
    }

    @Test
    fun testDigitHelpers() {
        val digits = "758294023"
        val component = GuessComponent(context, digits)

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
}
