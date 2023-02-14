package com.memorizepi

import androidx.compose.ui.window.Application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.memorizepi.components.NavigationComponent
import com.memorizepi.ui.NavigationScreen
import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.*

@Suppress("unused") //called from Swift
object EntryPoint {
    fun createComposeViewController(): UIViewController {
        initialize()
        val context = DefaultComponentContext(LifecycleRegistry())
        val navigation = NavigationComponent(context)

        return Application {
            NavigationScreen(navigation)
        }
    }

    fun main() {
        println("Starting")
        val args = emptyArray<String>()
        memScoped {
            val argc = args.size + 1
            val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
            autoreleasepool {
                UIApplicationMain(argc, argv, null, NSStringFromClass(SkikoAppDelegate))
            }
        }
    }

    class SkikoAppDelegate : UIResponder, UIApplicationDelegateProtocol {
        companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

        @ObjCObjectBase.OverrideInit
        constructor() : super()

        private var _window: UIWindow? = null
        override fun window() = _window
        override fun setWindow(window: UIWindow?) {
            _window = window
        }

        override fun application(application: UIApplication, didFinishLaunchingWithOptions: Map<Any?, *>?): Boolean {
            println("Creating window")
            window = UIWindow(frame = UIScreen.mainScreen.bounds)
            window!!.rootViewController = createComposeViewController()
            window!!.makeKeyAndVisible()
            return true
        }
    }
}