package com.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.memorizepi.models.PI_DIGITS
import com.memorizepi.repositories.SettingsRepo
import com.memorizepi.repositories.rounds.RoundRepository
import com.russhwolf.settings.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface Navigation {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class Menu(val component: MenuLogic): Child()
        class Guess(val component: GuessLogic): Child()
        class History(val component: HistoryLogic): Child()
        class Settings(val component: SettingsLogic): Child()
    }
}

class NavigationComponent(context: ComponentContext): Navigation, KoinComponent, ComponentContext by context {
    val router =
        router<Config, Navigation.Child>(
            initialConfiguration = Config.Menu,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    private val roundRepository by inject<RoundRepository>()

    override val routerState: Value<RouterState<*, Navigation.Child>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Navigation.Child =
        when (config) {
            is Config.Menu -> Navigation.Child.Menu(mainLogic())
            is Config.Guess -> Navigation.Child.Guess(guessLogic(componentContext, config.digits))
            is Config.History -> Navigation.Child.History(historyLogic(componentContext))
            is Config.Settings -> Navigation.Child.Settings(settingsLogic(componentContext))
        }

    private fun mainLogic() =
        object: MenuLogic {
            override fun goToGuess() {
                router.push(Config.Guess(PI_DIGITS))
            }

            override fun goToHistory() {
                router.push(Config.History)
            }

            override fun goToSettings() {
                router.push(Config.Settings)
            }
        }

    private fun guessLogic(context: ComponentContext, digits: String) =
        GuessComponent(context, digits, roundRepository, returnToMenu = {
            router.pop()
        })

    private fun historyLogic(context: ComponentContext) = HistoryComponent(context, roundRepository)

    private fun settingsLogic(context: ComponentContext) =
        SettingsComponent(context, SettingsRepo())

    sealed class Config : Parcelable {
        @Parcelize
        object Menu: Config()

        @Parcelize
        class Guess(val digits: String): Config()

        @Parcelize
        object History: Config()

        @Parcelize
        object Settings: Config()
    }
}
