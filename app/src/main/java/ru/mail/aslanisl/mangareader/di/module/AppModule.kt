package ru.mail.aslanisl.mangareader.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.MangaSourceFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideApiBuilder() = ApiBuilder()

    @Provides
    @Singleton
    fun provideSource(apiBuilder: ApiBuilder) = MangaSourceFactory.getSource(apiBuilder)
}