package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.memorizepi.models.Round
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.AppSettings.SortMethod
import com.memorizepi.repositories.SettingsRepo
import com.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.*

interface HistoryLogic {
    val state: Flow<HistoryState>
    val rounds: Flow<List<Round>>
    fun setSortMethod(sortMethod: SortMethod)
}

class HistoryComponent(private val context: ComponentContext,
                       roundRepository: RoundRepository,
                       settingsRepository: SettingsRepo):
    HistoryLogic, ComponentContext by context {

    private val mutableState =
        MutableStateFlow(HistoryState(sortMethod = settingsRepository.sortMethod))
    override val state: Flow<HistoryState>
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

    private val comparator = state.map {
        when(it.sortMethod) {
            SortMethod.NEWEST -> dateComparatorNewest
            SortMethod.OLDEST -> dateComparatorOldest
            SortMethod.BEST -> scoreComparatorBest
            SortMethod.WORST -> scoreComparatorWorst
        }
    }

    override val rounds = roundRepository.rounds.combine(comparator) { rounds, comparator ->
        rounds.sortedWith(comparator)
    }

    override fun setSortMethod(sortMethod: SortMethod) {
        mutableState.update {
            it.copy(sortMethod = sortMethod)
        }
    }
}

data class HistoryState(val sortMethod: SortMethod = SortMethod.NEWEST)