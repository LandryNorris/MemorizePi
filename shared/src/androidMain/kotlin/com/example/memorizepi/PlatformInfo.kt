package com.example.memorizepi

actual class PlatformInfo actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}