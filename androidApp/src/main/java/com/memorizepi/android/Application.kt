package com.memorizepi.android

import android.app.Application
import com.memorizepi.initialize

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }
}