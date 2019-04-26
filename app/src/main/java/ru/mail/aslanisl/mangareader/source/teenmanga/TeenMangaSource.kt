package ru.mail.aslanisl.mangareader.source.teenmanga

import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import ru.mail.aslanisl.mangareader.dataModel.Chapter
import ru.mail.aslanisl.mangareader.dataModel.Manga
import ru.mail.aslanisl.mangareader.dataModel.Page
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource

private const val BASE_URL = "https://teenmanga.ru/"

class TeenMangaSource(private val apiBuilder: ApiBuilder) : IMangaSource {

    private val api by lazy { apiBuilder.createRetrofit(BASE_URL).create(TeenMangaApi::class.java) }

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        val result = api.search(term).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }

        try {
            val json = JSONObject(result.body)
            val data = json["td_data"] as String
            val doc = Jsoup.parse(data)
            val elements = doc.getElementsByClass("td_module_mx2 td_module_wrap td-animation-stack")
            val mangas = mutableListOf<Manga>()
            elements?.forEach {
                val titleModule = it.getElementsByClass("entry-title td-module-title")[0]
                val thumb = it.getElementsByClass("td-module-thumb")[0]
                val manga = Manga(
                    titleModule.children().attr("href"),
                    thumb.children()[0].children().attr("src"),
                    titleModule.children().attr("title"),
                    ""
                )
                mangas.add(manga)
            }
            return UIData.success(mangas)
        } catch (e: Exception) {
            return UIData.errorMessage(e.message)
        }
    }

    override suspend fun loadChapter(idManga: String): UIData<List<Chapter>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadPages(idChapter: String): UIData<List<Page>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}