package ru.mail.aslanisl.mangareader

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mail.aslanisl.mangareader.db.Database
import ru.mail.aslanisl.mangareader.db.DatabaseBuilder
import ru.mail.aslanisl.mangareader.domain.MangaReadUseCase
import ru.mail.aslanisl.mangareader.features.details.DetailsViewModel
import ru.mail.aslanisl.mangareader.features.genre.GenreViewModel
import ru.mail.aslanisl.mangareader.features.history.HistoryViewModel
import ru.mail.aslanisl.mangareader.features.mangaList.MainViewModel
import ru.mail.aslanisl.mangareader.features.settings.SettingsViewModel
import ru.mail.aslanisl.mangareader.features.view.ChapterViewModel
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.MangaSourceFactory
import ru.mail.aslanisl.mangareader.utils.image.ImageCacheService

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    val appModule: Module = module {
        single { ApiBuilder() }
        single { MangaSourceFactory.getSource(get()) }
        single { DatabaseBuilder.build(get()) }
        single { (get() as Database).chapterReadDao() }
        single { (get() as Database).mangaReadDao() }
        viewModel { MainViewModel(get(), get()) }
        viewModel { DetailsViewModel(get(), get()) }
        viewModel { ChapterViewModel(get()) }
        viewModel { GenreViewModel(get(), get()) }
        viewModel { HistoryViewModel(get(), get()) }
        viewModel { SettingsViewModel(get()) }
        single { ImageCacheService(get()) }
        single { MangaReadUseCase(get()) }
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