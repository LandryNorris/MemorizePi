package com.example.memorizepi.android

import android.app.Application
import com.example.memorizepi.initialize

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }
}