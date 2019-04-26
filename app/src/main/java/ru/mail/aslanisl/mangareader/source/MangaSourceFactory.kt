package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.mangachan.MangaChanSource

object MangaSourceFactory {

    fun getSource(apiBuilder: ApiBuilder): IMangaSource {
        return MangaChanSource()
    }
}