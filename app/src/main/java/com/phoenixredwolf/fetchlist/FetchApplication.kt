package com.phoenixredwolf.fetchlist

import android.app.Application
import com.phoenixredwolf.fetchlist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * The main application class for the FetchList application.
 *
 * This class extends [Application] and is responsible for initializing the Koin dependency injection framework.
 * It sets up the Koin modules and provides application-level context.
 */
class FetchApplication : Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * (excluding content providers) have been created.
     *
     * This method initializes the Koin dependency injection framework by:
     * - Starting Koin.
     * - Enabling Android logging for Koin.
     * - Providing the application context to Koin.
     * - Loading the application's Koin modules ([appModule]).
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FetchApplication)
            modules(appModule)
        }
    }
}