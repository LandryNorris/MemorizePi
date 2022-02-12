package com.example.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

interface HistoryLogic {
    val state: Value<HistoryState>
    fun setSortMethod(sortMethod: SortMethod)
}

class HistoryComponent(private val context: ComponentContext,
                       private val roundRepository: RoundRepository):
    HistoryLogic, ComponentContext by context {
    private val mutableState = MutableValue(HistoryState())
    override val state: Value<HistoryState>
        get() = mutableState

    private val dateComparatorNewest = Comparator<Round> { a, b ->
        (b.timestamp - a.timestamp).toInt()
    }
    private val dateComparatorOldest = Comparator<Round> { a, b ->
        (a.timestamp - b.timestamp).toInt()
    }
    private val scoreComparatorBest = Comparator<Round> { a, b ->
        b.score - a.score
    }
    private val scoreComparatorWorst = Comparator<Round> { a, b ->
        a.score - b.score
    }

    val rounds = roundRepository.rounds.map {
        it.sortedWith(comparator)
    }

    private val comparator
        get() = when(state.value.sortMethod) {
            SortMethod.NEWEST -> dateComparatorNewest
            SortMethod.OLDEST -> dateComparatorOldest
            SortMethod.BEST -> scoreComparatorBest
            SortMethod.WORST -> scoreComparatorWorst
        }

    override fun setSortMethod(sortMethod: SortMethod) {
        mutableState.reduce {
            it.copy(sortMethod = sortMethod)
        }
    }
}

enum class SortMethod {
    NEWEST,
    OLDEST,
    BEST,
    WORST
}

data class HistoryState(val rounds: List<Round> = listOf(),
                        val sortMethod: SortMethod = SortMethod.NEWEST)