package com.example.yaran

import android.app.Application
import android.content.Context
import com.example.yaran.di.appModule
import com.example.yaran.di.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

class YaranApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        bind<Context>("ApplicationContext") with singleton { this@YaranApp.applicationContext }
        bind<YaranApp>() with singleton { this@YaranApp }
        import(appModule)
        import(networkModule)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}