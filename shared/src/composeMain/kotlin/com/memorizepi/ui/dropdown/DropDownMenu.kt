package com.memorizepi.ui.dropdown

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import com.memorizepi.ui.Popup
import kotlinx.coroutines.coroutineScope

@Composable
internal fun <T> DropDownMenu(label: String, value: T, values: List<T>,
                     getItemText: (T) -> String, itemSelected: (T) -> Unit,
                     isExpanded: Boolean, setExpanded: (Boolean) -> Unit) {
    DropdownMenuBox(expanded = isExpanded,
        onExpandedChange = setExpanded) {
        TextField(value = getItemText(value),
            onValueChange = {}, label = { Text(label) },
            readOnly = true)
        DropdownMenu(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            expanded = isExpanded,
            onDismissRequest = { setExpanded(false) }) {
            values.forEach {
                DropdownMenuItemContent(onClick = {
                    itemSelected(it)
                    setExpanded(false)
                }) {
                    Text(getItemText(it))
                }
            }
        }
    }
}

@Composable
internal fun DropdownMenuBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable DropdownMenuBoxScope.() -> Unit
) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(0) }
    var menuHeight by remember { mutableStateOf(0) }
    val coordinates = remember { Ref<LayoutCoordinates>() }

    val scope = remember(density, menuHeight, width) {
        object : DropdownMenuBoxScope {
            override fun Modifier.dropdownSize(matchTextFieldWidth: Boolean): Modifier {
                return with(density) {
                    heightIn(max = menuHeight.toDp()).let {
                        if (matchTextFieldWidth) {
                            it.width(width.toDp())
                        } else it
                    }
                }
            }
        }
    }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier.onGloballyPositioned {
            width = it.size.width
            coordinates.value = it
            menuHeight = 1920
        }.expandable(
            onExpandedChange = { onExpandedChange(!expanded) },
            menuLabel = "Dropdown Menu"
        ).focusRequester(focusRequester)
    ) {
        scope.content()
    }

    SideEffect {
        if (expanded) focusRequester.requestFocus()
    }
}

/**
 * Scope for [DropdownMenuBox].
 */
internal interface DropdownMenuBoxScope {
    /**
     * Modifier which should be applied to an [ExposedDropdownMenu]
     * placed inside the scope. It's responsible for
     * setting the width of the [ExposedDropdownMenu], which
     * will match the width of the [TextField]
     * (if [matchTextFieldWidth] is set to true).
     * Also it'll change the height of [ExposedDropdownMenu], so
     * it'll take the largest possible height to not overlap
     * the [TextField] and the software keyboard.
     *
     * @param matchTextFieldWidth Whether menu should match
     * the width of the text field to which it's attached.
     * If set to true the width will match the width
     * of the text field.
     */
    fun Modifier.dropdownSize(
        matchTextFieldWidth: Boolean = true
    ): Modifier

    /**
     * Popup which contains content for Exposed Dropdown Menu.
     * Should be used inside the content of [ExposedDropdownMenuBox].
     *
     * @param expanded Whether the menu is currently open and visible to the user
     * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
     * tapping outside the menu's bounds
     * @param modifier The modifier to apply to this layout
     * @param content The content of the [DropdownMenu]
     */
    @Composable
    fun DropdownMenu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        val expandedStates = remember { MutableTransitionState(false) }
        expandedStates.targetState = expanded

        if (expandedStates.currentState || expandedStates.targetState) {
            val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }
            val density = LocalDensity.current
            val popupPositionProvider = DropdownMenuPositionProvider(
                DpOffset.Zero,
                density
            ) { parentBounds, menuBounds ->
                transformOriginState.value = calculateTransformOrigin(parentBounds, menuBounds)
            }

            println("Showing popup")

            Popup(
                onDismissRequest = onDismissRequest,
                popupPositionProvider = popupPositionProvider
            ) {
                com.memorizepi.ui.dropdown.DropdownMenuContent(
                    expandedStates = expandedStates,
                    transformOriginState = transformOriginState,
                    modifier = modifier.dropdownSize(),
                    content = content
                )
            }
        }
    }
}

private fun Modifier.expandable(
    onExpandedChange: () -> Unit,
    menuLabel: String
) = pointerInput(Unit) {
    forEachGesture {
        coroutineScope {
            awaitPointerEventScope {
                var event: PointerEvent
                do {
                    event = awaitPointerEvent(PointerEventPass.Initial)
                } while (
                    !event.changes.all { it.changedToUp() }
                )
                onExpandedChange.invoke()
            }
        }
    }
}.semantics {
    contentDescription = menuLabel // this should be a localised string
    onClick {
        onExpandedChange()
        true
    }
}
