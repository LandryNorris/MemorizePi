package com.memorizepi.components

import com.memorizepi.models.PI_DIGITS
import com.russhwolf.settings.Settings

interface MenuLogic {
    fun goToGuess(digits: String = PI_DIGITS)
    fun goToHistory(settingsType: SettingsType = SettingsType.RealSettings)
    fun goToSettings(settingsType: SettingsType = SettingsType.RealSettings)
}
