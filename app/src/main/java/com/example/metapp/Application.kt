package com.example.metapp

import android.app.Application
import org.koin.core.context.startKoin

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // TODO: Declare modules
        }
    }

}