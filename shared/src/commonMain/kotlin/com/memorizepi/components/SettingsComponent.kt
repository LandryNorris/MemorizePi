package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.AppSettings.Constant
import com.memorizepi.repositories.AppSettings.SortMethod
import com.memorizepi.repositories.SettingsRepo
import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface SettingsLogic {
    val state: Flow<SettingsState>

    fun setConstant(constant: Constant)
    fun setSortMethod(sortMethod: SortMethod)
    fun setConstantExpanded(expanded: Boolean)
    fun setSortMethodExpanded(expanded: Boolean)
}

sealed class SettingsType: Parcelable {
    abstract fun createSettings(): Settings
    @Parcelize
    object RealSettings: SettingsType(), Parcelable {
        override fun createSettings() = Settings()
    }
    @Parcelize
    object MockSettings: SettingsType(), Parcelable {
        override fun createSettings() = MockSettings()
    }
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

    override fun setConstantExpanded(expanded: Boolean) {
        state.update { it.copy(isConstantBoxExpanded = expanded) }
    }

    override fun setSortMethodExpanded(expanded: Boolean) {
        state.update { it.copy(isSortMethodBoxExpanded = expanded) }
    }
}

data class SettingsState(val constant: Constant = Constant.PI,
                         val constants: List<Constant> =
                             listOf(Constant.PI, Constant.E, Constant.SQRT2),
                         val sortMethod: SortMethod = SortMethod.NEWEST,
                         val sortMethods: List<SortMethod> =
                             listOf(SortMethod.NEWEST, SortMethod.OLDEST,
                                 SortMethod.BEST, SortMethod.WORST),
                         val isConstantBoxExpanded: Boolean = false,
                         val isSortMethodBoxExpanded: Boolean = false)