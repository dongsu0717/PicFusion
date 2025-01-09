package com.dongsu.picfusion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PicFusionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(TimberDebugTree())
    }
}

class TimberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "${element.fileName}#${element.lineNumber}, ${element.methodName}"
    }
}
