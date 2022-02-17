package com.memorizepi.components

import com.memorizepi.repositories.AppSettings

interface MenuLogic {
    fun goToGuess(settings: SettingsType = SettingsType.RealSettings)
    fun goToHistory(settingsType: SettingsType = SettingsType.RealSettings)
    fun goToSettings(settingsType: SettingsType = SettingsType.RealSettings)
}
