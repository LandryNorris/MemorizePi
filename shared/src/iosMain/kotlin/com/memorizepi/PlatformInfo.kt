package com.memorizepi

import platform.UIKit.UIDevice

actual class PlatformInfo actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}