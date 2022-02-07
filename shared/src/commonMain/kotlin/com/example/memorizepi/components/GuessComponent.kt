package com.example.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.example.memorizepi.models.PI_DIGITS

interface GuessLogic {
    val state: Value<GuessState>

    fun guessDigit(digit: Char)
}

class GuessComponent(private val context: ComponentContext, digits: String):
    GuessLogic, ComponentContext by context {
    private val mutableState = MutableValue(GuessState(digits = digits))
    override val state: Value<GuessState> = mutableState

    override fun guessDigit(digit: Char) {
        if(!digit.isDigit()) throw IllegalArgumentException("Entry is not a digit")
        val currentDigit = state.value.currentDigit
        if(digit == currentDigit) {
            mutableState.reduce {
                it.copy(currentScore = it.currentScore+1)
            }
        } else {
            mutableState.reduce {
                it.copy(numIncorrect = it.numIncorrect+1)
            }
        }
    }
}

data class GuessState(
    val digits: String = PI_DIGITS,
    val currentScore: Int = 0,
    val bestScore: Int = 0,
    val numIncorrect: Int = 0
) {
    val currentDigit: Char
        get() = digits[currentScore]

    val lastDigit: Char?
        get() = if(currentScore > 0) digits[currentScore-1] else null

    val secondLastDigit: Char?
        get() = if(currentScore > 1) digits[currentScore-2] else null
}
