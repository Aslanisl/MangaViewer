package ru.mail.aslanisl.mangareader.di.component

import dagger.Component
import ru.mail.aslanisl.mangareader.di.module.AppModule
import ru.mail.aslanisl.mangareader.features.details.MangaDetailsActivity
import ru.mail.aslanisl.mangareader.features.search.MainActivity
import ru.mail.aslanisl.mangareader.features.view.ChapterActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MangaDetailsActivity)
    fun inject(activity: ChapterActivity)
}