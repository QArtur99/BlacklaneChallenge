package com.qartf.blacklanechallenge

import android.app.Application
import com.qartf.blacklanechallenge.di.NetworkModule
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()

        GlobalScope
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(NetworkModule))
        }
    }
}