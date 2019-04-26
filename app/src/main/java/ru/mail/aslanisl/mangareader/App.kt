package ru.mail.aslanisl.mangareader

import android.app.Application
import ru.mail.aslanisl.mangareader.di.component.AppComponent
import ru.mail.aslanisl.mangareader.di.module.AppModule
import ru.mail.aslanisl.mangareader.di.component.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    val appComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build() as AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}