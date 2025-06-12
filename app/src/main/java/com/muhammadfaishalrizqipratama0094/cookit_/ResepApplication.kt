package com.muhammadfaishalrizqipratama0094.cookit_

import android.app.Application

class ResepApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}