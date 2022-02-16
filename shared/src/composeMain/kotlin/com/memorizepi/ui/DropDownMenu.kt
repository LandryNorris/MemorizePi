package com.memorizepi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropDownMenu(label: String, value: T, values: List<T>,
                     getItemText: (T) -> String, itemSelected: (T) -> Unit,
                     isExpanded: Boolean, setExpanded: (Boolean) -> Unit) {
    ExposedDropdownMenuBox(expanded = isExpanded,
        onExpandedChange = setExpanded) {
        TextField(value = getItemText(value),
            onValueChange = {}, label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            })
        ExposedDropdownMenu(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            expanded = isExpanded,
            onDismissRequest = { setExpanded(false) }) {
            values.forEach {
                DropdownMenuItem(onClick = {
                    itemSelected(it)
                    setExpanded(false)
                }) {
                    Text(getItemText(it))
                }
            }
        }
    }
}