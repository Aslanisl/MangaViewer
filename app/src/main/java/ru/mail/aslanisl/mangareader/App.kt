package ru.mail.aslanisl.mangareader

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mail.aslanisl.mangareader.features.details.DetailsViewModel
import ru.mail.aslanisl.mangareader.features.search.MainViewModel
import ru.mail.aslanisl.mangareader.features.view.ChapterViewModel
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource
import ru.mail.aslanisl.mangareader.source.MangaSourceFactory
import ru.mail.aslanisl.mangareader.source.ninemanga.NineMangaSource

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    val appModule: Module = module {
        single { ApiBuilder() }
        single { MangaSourceFactory.getSource(get()) }
        viewModel { MainViewModel(get()) }
        viewModel { DetailsViewModel(get()) }
        viewModel { ChapterViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}