package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.AppSettings.Constant
import com.memorizepi.repositories.AppSettings.SortMethod
import com.memorizepi.repositories.SettingsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface SettingsLogic {
    val state: Flow<SettingsState>

    fun setConstant(constant: Constant)
    fun setSortMethod(sortMethod: SortMethod)
}

class SettingsComponent(private val context: ComponentContext,
                        private val repo: SettingsRepo): SettingsLogic,
    ComponentContext by context {

    override val state = MutableStateFlow(
        SettingsState(constant = repo.constant, sortMethod = repo.sortMethod)
    )

    override fun setConstant(constant: Constant) {
        repo.constant = constant
        state.update {
            it.copy(constant = constant)
        }
    }

    override fun setSortMethod(sortMethod: SortMethod) {
        repo.sortMethod = sortMethod
        state.update {
            it.copy(sortMethod = sortMethod)
        }
    }
}

data class SettingsState(val constant: Constant = Constant.PI,
                         val constants: List<Constant> =
                             listOf(Constant.PI, Constant.E, Constant.SQRT2),
                         val sortMethod: SortMethod = SortMethod.NEWEST)