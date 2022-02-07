package com.example.memorizepi.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.example.memorizepi.models.PI_DIGITS

interface Navigation {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class Menu(val component: MenuLogic): Child()
        class Guess(val component: GuessLogic): Child()
    }
}

class NavigationComponent(context: ComponentContext): Navigation, ComponentContext by context {
    private val router =
        router<Config, Navigation.Child>(
            initialConfiguration = Config.Menu,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Navigation.Child>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Navigation.Child =
        when (config) {
            is Config.Menu -> Navigation.Child.Menu(mainLogic())
            is Config.Guess -> Navigation.Child.Guess(guessLogic(componentContext, config.digits))
        }

    private fun mainLogic() =
        object: MenuLogic {
            override fun goToGuess() {
                router.push(Config.Guess(PI_DIGITS))
            }
        }

    private fun guessLogic(context: ComponentContext, digits: String) =
        GuessComponent(context, digits, returnToMenu = {
            router.pop()
        })

    private sealed class Config : Parcelable {
        @Parcelize
        object Menu: Config()

        @Parcelize
        class Guess(val digits: String): Config()
    }
}
