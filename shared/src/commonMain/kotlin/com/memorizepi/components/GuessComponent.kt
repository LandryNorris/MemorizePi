package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.memorizepi.models.getDigits
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

interface GuessLogic {
    val state: Flow<GuessState>

    fun guessDigit(digit: Char) {}
    fun returnToMenu() {}
    fun retry() {}
}

class GuessComponent(private val context: ComponentContext,
                     private val constant: AppSettings.Constant,
                     private val roundRepository: RoundRepository,
                     private val returnToMenu: () -> Unit):
    GuessLogic, ComponentContext by context {
    private val mutableState = MutableStateFlow(initGame())
    override val state = mutableState

    private val clock = Clock.System

    override fun guessDigit(digit: Char) {
        if(!digit.isDigit()) throw IllegalArgumentException("Entry is not a digit")
        val currentDigit = state.value.currentDigit

        //handle start time.
        if(state.value.startTime == 0L) mutableState.update {
            it.copy(startTime = clock.now().toEpochMilliseconds())
        }

        //check guess
        if(digit == currentDigit) {
            mutableState.update {
                val newScore = it.currentScore+1
                it.copy(currentScore = newScore, bestScore = maxOf(it.bestScore, newScore))
            }
        } else { //incorrect guess
            mutableState.update {
                it.copy(numIncorrect = it.numIncorrect+1)
            }

            if(state.value.numIncorrect >= state.value.numIncorrectAllowed) {
                onGameOver()
            }
        }
    }

    override fun returnToMenu() = returnToMenu.invoke()

    override fun retry() {
        mutableState.update {
            initGame()
        }
    }

    private fun initGame() = GuessState(bestScore = roundRepository.topScore(constant),
        gameOver = false, constant = constant)

    private fun onGameOver() {
        mutableState.update {
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
