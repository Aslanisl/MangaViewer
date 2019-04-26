package ru.mail.aslanisl.mangareader.source.ninemanga

import org.json.JSONArray
import org.jsoup.Jsoup
import ru.mail.aslanisl.mangareader.dataModel.Chapter
import ru.mail.aslanisl.mangareader.dataModel.Manga
import ru.mail.aslanisl.mangareader.dataModel.Page
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource

private const val BASE_URL = "http://ru.ninemanga.com/"
private const val BASE_URL_WITHOUT_LINE = "http://ru.ninemanga.com"
private const val BASE_URL_PHOTO = "https://ruimg.taadd.com"

// If load diff when have parse block from site
private const val MAX_COUNT_LOAD_PAGES = 10

class NineMangaSource constructor(private val apiBuilder: ApiBuilder) : IMangaSource {

    private val api by lazy { apiBuilder.createRetrofit(BASE_URL).create(NineMangaApi::class.java) }

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        val result = api.search(term).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }

        return try {
            val mangas = mutableListOf<Manga>()
            val jsonArray = JSONArray(result.body)
            for (i in 0 until jsonArray.length()) {
                val element = jsonArray[i] as JSONArray
                val manga = Manga(
                    element[1] as String,
                    convertPhotoUrl(element[0] as String),
                    element[1] as String,
                    element[3] as String
                )
                mangas.add(manga)
            }
            UIData.success(mangas)
        } catch (e: Exception) {
            UIData.errorMessage(e.message)
        }
    }

    private fun convertPhotoUrl(url: String): String {
        return BASE_URL_PHOTO + url
    }

    override suspend fun loadChapter(idManga: String): UIData<List<Chapter>> {
        val result = api.load(idManga).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }

        return try {
            val document = Jsoup.parse(result.body)
            val elements = document.getElementsByClass("sub_vol_ul")
            val parsedChars = elements
                .flatMap { it.children() }
                .map {
                    val chapterList = it.getElementsByClass("chapter_list_a")[0]
                    Chapter(chapterList.attr("href"), chapterList.attr("title"))
                }
            UIData.success(parsedChars)
        } catch (e: Exception) {
            UIData.errorMessage(e.message)
        }
    }

    override suspend fun loadPages(idChapter: String): UIData<List<Page>> {
        val result = api.loadChapter(idChapter).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }

        return try {
            val document = Jsoup.parse(result.body)
            val pagesCount = document
                .getElementsByClass("changepage")[0]
                .getElementById("page")
                .children()
                .size

            val pages = mutableListOf<Page>()
            val loadTimes = pagesCount / MAX_COUNT_LOAD_PAGES + 1
            for (i in 0 until loadTimes) {
                val url = idChapter.replace(".html", "-$MAX_COUNT_LOAD_PAGES-${i + 1}.html")

                var chapterResult = api.loadChapter(url).await()
                if (chapterResult.isSuccess().not()) {
                    chapterResult = api.loadChapter(url).await()
                    if (chapterResult.isSuccess().not()) break
                }

                val documentChapter = Jsoup.parse(chapterResult.body)
                for (j in 1..MAX_COUNT_LOAD_PAGES) {
                    val element = documentChapter.getElementById("manga_pic_$i")
                    element?.attr("src")?.let {
                        val page = Page((i + j).toString(), i + j, it)
                        pages.add(page)
                    }
                }
            }

            UIData.success(pages)
        } catch (e: Exception) {
            UIData.errorMessage(e.message)
        }
    }
}