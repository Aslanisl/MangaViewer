package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.ninemanga.NineMangaSource

object MangaSourceFactory {

    fun getSource(apiBuilder: ApiBuilder): IMangaSource {
        return NineMangaSource(apiBuilder)
    }
}