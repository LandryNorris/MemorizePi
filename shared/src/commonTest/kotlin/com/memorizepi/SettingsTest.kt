package com.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.memorizepi.components.SettingsComponent
import com.memorizepi.repositories.AppSettings.Constant.*
import com.memorizepi.repositories.AppSettings.SortMethod
import com.memorizepi.repositories.SettingsRepo
import com.russhwolf.settings.MockSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsTest {
    val context = DefaultComponentContext(LifecycleRegistry())
    private val mockSettings
        get() = MockSettings()
    private val settingsRepo
        get() = SettingsRepo(mockSettings)

    @Test
    fun testCreateComponent() {
        val settingsComponent = SettingsComponent(context, settingsRepo)
        val initialState = settingsComponent.state.value

        assertEquals(PI, initialState.constant)
        assertEquals(SortMethod.NEWEST, initialState.sortMethod)
    }

    @Test
    fun testWriteConstant() {
        val settingsComponent = SettingsComponent(context, settingsRepo)
        val initialState = settingsComponent.state.value
        assertEquals(PI, initialState.constant)

        settingsComponent.setConstant(E)
        assertEquals(E, settingsComponent.state.value.constant)

        settingsComponent.setConstant(SQRT2)
        assertEquals(SQRT2, settingsComponent.state.value.constant)
    }

    @Test
    fun testWriteSortMethod() {
        val settingsComponent = SettingsComponent(context, settingsRepo)
        val initialState = settingsComponent.state.value
        assertEquals(SortMethod.NEWEST, initialState.sortMethod)

        settingsComponent.setSortMethod(SortMethod.BEST)
        assertEquals(SortMethod.BEST, settingsComponent.state.value.sortMethod)
    }

    @Test
    fun testWritesAcrossCreatingRepos() {
        val settings = mockSettings
        val repo1 = SettingsRepo(settings)
        val component1 = SettingsComponent(context, repo1)

        component1.setConstant(SQRT2)
        component1.setSortMethod(SortMethod.WORST)

        val repo2 = SettingsRepo(settings)
        val component2 = SettingsComponent(context, repo2)

        assertEquals(SQRT2, component2.state.value.constant)
        assertEquals(SortMethod.WORST, component2.state.value.sortMethod)
    }
}