package com.memorizepi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.memorizepi.components.SettingsLogic
import com.memorizepi.components.SettingsState
import com.memorizepi.repositories.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(logic: SettingsLogic) {
    val state by logic.state.collectAsState(initial = SettingsState())
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        DropDownMenu(
            label = "Constant",
            value = state.constant,
            values = state.constants,
            getItemText = { it.name },
            itemSelected = logic::setConstant,
            isExpanded = state.isConstantBoxExpanded,
            setExpanded = logic::setConstantExpanded
        )

        DropDownMenu(
            label = "Constant",
            value = state.sortMethod,
            values = state.sortMethods,
            getItemText = { it.name },
            itemSelected = logic::setSortMethod,
            isExpanded = state.isSortMethodBoxExpanded,
            setExpanded = logic::setSortMethodExpanded
        )
    }
}

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(logic = object : SettingsLogic {
        override val state = MutableStateFlow(
            SettingsState()
        )

        override fun setConstant(constant: AppSettings.Constant) {}
        override fun setSortMethod(sortMethod: AppSettings.SortMethod) {}
        override fun setConstantExpanded(expanded: Boolean) {}
        override fun setSortMethodExpanded(expanded: Boolean) {}
    })
}