package com.qartf.blacklanechallenge

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class TestApp : Application() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        loadKoin()
    }

    private fun loadKoin() {
        startKoin {
            androidLogger()
            androidContext(this@TestApp)
        }
    }

    internal fun injectModule(module: Module) {
        loadKoinModules(module)
    }
}
