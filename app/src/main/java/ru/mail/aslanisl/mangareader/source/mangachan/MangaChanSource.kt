package ru.mail.aslanisl.mangareader.source.mangachan

import org.jsoup.Jsoup
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource
import java.lang.Exception

private const val BASE_URL = "https://mangachan.me/"

class MangaChanSource : IMangaSource {

    private val api by lazy { ApiBuilder().createRetrofit(BASE_URL).create(MangaChanApi::class.java) }

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        val result = api.request("https://mangachan.me/?do=search&subaction=search&story=$term").await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        val mangas = mutableListOf<Manga>()
        try {
            val doc = Jsoup.parse(result.body)
            val contentElem = doc.getElementsByClass("content_row")
            contentElem?.forEach {
                val title = it.attr("title")
                val imageUrl = it.getElementsByClass("manga_images")[0].children()[0].attr("src")
                val description = it.getElementsByClass("tags").getOrNull(0)?.children()?.getOrNull(0)?.textNodes()?.getOrNull(0)?.wholeText ?: ""
                val link = it.getElementsByClass("manga_row1")[0].children()[0].children()[1].children()[0].attr("href")
                val manga = Manga(link, imageUrl, title, description)
                mangas.add(manga)
            }
            return UIData.success(mangas)
        } catch (e: Exception) {
            if (mangas.isEmpty().not()) {
                return UIData.success(mangas)
            }
            return UIData.errorMessage(e.message)
        }
    }

    override suspend fun loadChapter(idManga: String): UIData<List<Chapter>> {
        val result = api.request(idManga).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        val chapters = mutableListOf<Chapter>()
        try {
            val doc = Jsoup.parse(result.body)
            val tables = doc.getElementsByClass("table_cha")
            tables.forEach { table ->
                val mangas = table.getElementsByClass("manga2")
                mangas.forEach { manga ->
                    val href = manga.children()[0].attr("href")
                    val title = manga.children()[0].textNodes()[0].wholeText
                    val chapter = Chapter(href, title)
                    chapters.add(chapter)
                }
            }
            return UIData.success(chapters)
        } catch (e: Exception) {
            if (chapters.isEmpty().not()) {
                return UIData.success(chapters)
            }
            return UIData.errorMessage(e.message)
        }
    }

    override suspend fun loadPages(idChapter: String): UIData<List<Page>> {
        val result = api.request(idChapter).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        val pages = mutableListOf<Page>()
        try {
            val textFirst = result.body!!.substringAfter("\"fullimg\":[")
            val textSecond = textFirst.split("]")[0]
            val pagesUrl = textSecond
                .replace("\"", "")
                .split(",")
                .filterNot { it.isEmpty() }
            pagesUrl.forEachIndexed { index, url ->
                val page = Page(url, index + 1, url)
                pages.add(page)
            }
            return UIData.success(pages)
        } catch (e: Exception) {
            if (pages.isEmpty().not()) {
                return UIData.success(pages)
            }
            return UIData.errorMessage(e.message)
        }
    }
}