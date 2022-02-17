package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.memorizepi.models.getDigits
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.rounds.RoundRepository
import kotlinx.datetime.Clock

interface GuessLogic {
    val state: Value<GuessState>

    fun guessDigit(digit: Char) {}
    fun returnToMenu() {}
    fun retry() {}
}

class GuessComponent(private val context: ComponentContext,
                     private val constant: AppSettings.Constant,
                     private val roundRepository: RoundRepository,
                     private val returnToMenu: () -> Unit):
    GuessLogic, ComponentContext by context {
    private val mutableState = MutableValue(initGame())
    override val state: Value<GuessState> = mutableState

    private val clock = Clock.System

    override fun guessDigit(digit: Char) {
        if(!digit.isDigit()) throw IllegalArgumentException("Entry is not a digit")
        val currentDigit = state.value.currentDigit

        //handle start time.
        if(state.value.startTime == 0L) mutableState.reduce {
            it.copy(startTime = clock.now().toEpochMilliseconds())
        }

        //check guess
        if(digit == currentDigit) {
            mutableState.reduce {
                val newScore = it.currentScore+1
                it.copy(currentScore = newScore, bestScore = maxOf(it.bestScore, newScore))
            }
        } else { //incorrect guess
            mutableState.reduce {
                it.copy(numIncorrect = it.numIncorrect+1)
            }

            if(state.value.numIncorrect >= state.value.numIncorrectAllowed) {
                onGameOver()
            }
        }
    }

    override fun returnToMenu() = returnToMenu.invoke()

    override fun retry() {
        mutableState.reduce {
            initGame()
        }
    }

    private fun initGame() = GuessState(bestScore = roundRepository.topScore(constant),
        gameOver = false, constant = constant)

    private fun onGameOver() {
        mutableState.reduce {
            it.copy(gameOver = true)
        }
        roundRepository.saveGame(state.value)
    }
}

data class GuessState(
    val constant: AppSettings.Constant = AppSettings.Constant.PI,
    val numIncorrectAllowed: Int = 3,
    val startTime: Long = 0L,
    val currentScore: Int = 0,
    val bestScore: Int = 0,
    val numIncorrect: Int = 0,
    val gameOver: Boolean = false
) {
    private val digits: String = getDigits(constant)

    val currentDigit: Char
        get() = digits[currentScore]

    /**
     * @param index How many spots back to look. 0 gives the last digit, 1 gives the second last,
     * and so on.
     */
    fun lastDigit(index: Int) =
        if(currentScore > index) digits[currentScore - (index+1)] else null
}
