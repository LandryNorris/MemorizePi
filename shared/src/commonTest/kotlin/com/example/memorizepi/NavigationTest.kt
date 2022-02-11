package com.example.memorizepi

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.push
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.memorizepi.components.GuessState
import com.example.memorizepi.components.Navigation
import com.example.memorizepi.components.NavigationComponent
import com.example.memorizepi.models.Round
import com.example.memorizepi.repositories.rounds.RoundRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class NavigationTest {
    private val context = DefaultComponentContext(LifecycleRegistry())
    private val koinModule = module {
        single {
            object: RoundRepository() {
                override fun saveGame(state: GuessState) {}

                override val rounds = MutableSharedFlow<List<Round>>()
                override val topScore = 0
            } as RoundRepository
        }
    }

    @BeforeTest
    fun init() {
        startKoin {
            modules(koinModule)
        }
    }

    @AfterTest
    fun cleanup() { stopKoin() }

    @Test
    fun testInitialRoute() {
        val navComponent = NavigationComponent(context)
        assertTrue(navComponent.routerState.value.activeChild.instance is Navigation.Child.Menu)
    }

    @Test
    fun testNavigateToGuess() {
        val navComponent = NavigationComponent(context)
        val config = NavigationComponent.Config.Guess("123456")
        navComponent.router.push(config)
        assertTrue(navComponent.routerState.value.activeChild.instance is Navigation.Child.Guess)
    }

    @Test
    fun testNavigateViaMenuLogic() {
        val navComponent = NavigationComponent(context)
        val menuConfig = navComponent.router.state.value.activeChild.instance
                as? Navigation.Child.Menu
        menuConfig?.component?.goToGuess()
        assertTrue(navComponent.routerState.value.activeChild.instance is Navigation.Child.Guess)
    }

    @Test
    fun testReturnToMenuFromGuess() {
        val navComponent = NavigationComponent(context)
        val menuConfig = navComponent.router.state.value.activeChild.instance
                as? Navigation.Child.Menu
        menuConfig?.component?.goToGuess()
        val guessConfig = navComponent.router.state.value.activeChild.instance
                as? Navigation.Child.Guess
        guessConfig?.component?.returnToMenu()
        assertTrue(navComponent.routerState.value.activeChild.instance is Navigation.Child.Menu)
    }
}