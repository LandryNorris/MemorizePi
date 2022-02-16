package com.memorizepi.repositories

import com.memorizepi.repositories.AppSettings.Constant
import com.memorizepi.repositories.AppSettings.SortMethod
import com.memorizepi.repositories.AppSettings.constantTag
import com.memorizepi.repositories.AppSettings.sortMethodTag
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

/*
 * Do NOT change the order of any enum constants. The ordinal is saved in settings.
 */
object AppSettings {
    const val constantTag = "Constant"
    enum class Constant {
        PI,
        E,
        SQRT2
    }

    const val sortMethodTag = "SortMethod"
    enum class SortMethod {
        NEWEST,
        OLDEST,
        BEST,
        WORST
    }
}

class SettingsRepo(private val settings: Settings = Settings()) {
    private fun save(tag: String, value: Int) {
        settings[tag] = value
    }

    var sortMethod: SortMethod
        get() = SortMethod.values()[settings[sortMethodTag] ?: SortMethod.NEWEST.ordinal]
        set(value) { save(sortMethodTag, value.ordinal) }

    var constant: Constant
        get() = Constant.values()[settings[constantTag] ?: Constant.PI.ordinal]
        set(value) { save(constantTag, value.ordinal) }
}