package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.data.base.UIData

interface IMangaSource {
    suspend fun searchManga(term: String): UIData<List<Manga>>
    suspend fun loadChapter(idManga: String): UIData<List<Chapter>>
    suspend fun loadPages(idChapter: String): UIData<List<Page>>
}