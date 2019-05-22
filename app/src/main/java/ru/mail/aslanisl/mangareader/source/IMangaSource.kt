package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.MangaDetails
import ru.mail.aslanisl.mangareader.data.model.Page

interface IMangaSource {
    suspend fun searchManga(term: String): UIData<List<Manga>>

    suspend fun loadGenres(): UIData<List<Genre>>
    suspend fun loadMangaGenre(genreId: String): UIData<List<Manga>>
    suspend fun loadMangaGenre(genreId: String, offset: Int): UIData<List<Manga>>
    fun genrePagingCount(): Int

    suspend fun loadMangaDetails(idManga: String): UIData<MangaDetails>
    suspend fun loadPages(idChapter: String): UIData<List<Page>>
}