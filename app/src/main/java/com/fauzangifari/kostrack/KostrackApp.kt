package com.fauzangifari.kostrack

import android.app.Application
import com.fauzangifari.kostrack.di.viewModelModule
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class KostrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KostrackApp)
            modules(
                listOf(
                    viewModelModule,
                )
            )
        }
    }
}