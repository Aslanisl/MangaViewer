package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre

interface IMangaSource {
    suspend fun searchManga(term: String): UIData<List<Manga>>

    suspend fun loadGenres(): UIData<List<Genre>>
    suspend fun loadMangaGenre(genreId: String): UIData<List<Manga>>
    suspend fun loadMangaGenre(genreId: String, offset: Int): UIData<List<Manga>>
    fun genrePagingCount(): Int

    suspend fun loadChapter(idManga: String): UIData<List<Chapter>>
    suspend fun loadPages(idChapter: String): UIData<List<Page>>
}