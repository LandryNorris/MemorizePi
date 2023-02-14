package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.memorizepi.repositories.AppSettings
import com.memorizepi.repositories.SettingsRepo
import com.memorizepi.repositories.rounds.RoundRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface Navigation {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Menu(val component: MenuLogic): Child()
        class Guess(val component: GuessLogic): Child()
        class History(val component: HistoryLogic): Child()
        class Settings(val component: SettingsLogic): Child()
    }
}

class NavigationComponent(context: ComponentContext): Navigation, KoinComponent, ComponentContext by context {
    val navigation = StackNavigation<Config>()
    override val childStack =
        childStack(
            source = navigation,
            initialConfiguration = Config.Menu,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    private val roundRepository by inject<RoundRepository>()

    private fun createChild(config: Config, componentContext: ComponentContext): Navigation.Child =
        when (config) {
            is Config.Menu -> Navigation.Child.Menu(mainLogic())
            is Config.Guess -> Navigation.Child.Guess(guessLogic(componentContext, config.constant))
            is Config.History -> Navigation.Child.History(
                historyLogic(componentContext, config.settingsType))
            is Config.Settings -> Navigation.Child.Settings(settingsLogic(componentContext, config.settingsType))
        }

    private fun mainLogic() =
        object: MenuLogic {
            override fun goToGuess(settings: SettingsType) {
                val settingsRepo = SettingsRepo(settings.createSettings())
                navigation.push(Config.Guess(settingsRepo.constant))
            }

            override fun goToHistory(settingsType: SettingsType) {
                navigation.push(Config.History(settingsType))
            }

            override fun goToSettings(settingsType: SettingsType) {
                navigation.push(Config.Settings(settingsType))
            }
        }

    private fun guessLogic(context: ComponentContext, constant: AppSettings.Constant) =
        GuessComponent(context, constant, roundRepository, returnToMenu = {
            navigation.pop()
        })

    private fun historyLogic(context: ComponentContext, settingsType: SettingsType) =
        HistoryComponent(context, roundRepository, SettingsRepo(settingsType.createSettings()))

    private fun settingsLogic(context: ComponentContext, settingsType: SettingsType) =
        SettingsComponent(context, SettingsRepo(settingsType.createSettings()))

    sealed class Config : Parcelable {
        @Parcelize
        object Menu: Config()

        @Parcelize
        class Guess(val constant: AppSettings.Constant): Config()

        @Parcelize
        class History(val settingsType: SettingsType): Config()

        @Parcelize
        class Settings(val settingsType: SettingsType): Config()
    }
}

