package org.sea.rawg

import android.app.Application
import org.sea.rawg.di.AppModule

class RawgApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        AppModule.init()
    }
}