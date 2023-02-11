package com.memorizepi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.memorizepi.components.SettingsLogic
import com.memorizepi.components.SettingsState
import com.memorizepi.ui.dropdown.DropDownMenu

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
