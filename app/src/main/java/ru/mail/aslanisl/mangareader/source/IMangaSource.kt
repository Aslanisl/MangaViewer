package ru.mail.aslanisl.mangareader.source

import ru.mail.aslanisl.mangareader.dataModel.Chapter
import ru.mail.aslanisl.mangareader.dataModel.Manga
import ru.mail.aslanisl.mangareader.dataModel.Page
import ru.mail.aslanisl.mangareader.dataModel.base.UIData

interface IMangaSource {
    suspend fun searchManga(term: String): UIData<List<Manga>>
    suspend fun loadChapter(idManga: String): UIData<List<Chapter>>
    suspend fun loadPages(idChapter: String): UIData<List<Page>>
}