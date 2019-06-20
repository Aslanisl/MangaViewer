package ru.mail.aslanisl.mangareader.source.mangachan

import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.MangaDetails
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource
import ru.mail.aslanisl.mangareader.source.MangaFilter
import ru.mail.aslanisl.mangareader.source.MangaFilter.CHAPTER
import ru.mail.aslanisl.mangareader.source.MangaFilter.DATE
import ru.mail.aslanisl.mangareader.source.MangaFilter.FAVORITE
import ru.mail.aslanisl.mangareader.source.MangaFilter.NAME
import ru.mail.aslanisl.mangareader.source.MangaFilter.NONE

private const val BASE_URL = "https://manga-chan.me/"
private const val PAGINATION_COUNT = 20

class MangaChanSource : IMangaSource {

    private val api by lazy { ApiBuilder().createRetrofit(BASE_URL).create(MangaChanApi::class.java) }

    override suspend fun loadGenres(): UIData<List<Genre>> {
        val result = api.request("https://mangachan.me/tags/").await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        val genres = mutableListOf<Genre>()
        try {
            val doc = Jsoup.parse(result.body)
            val news = doc.getElementsByClass("news")[0]
            news.children().forEach {
                val href = it.attr("href")
                val title = it.text()?.replace("_", " ")
                if (href.isNullOrEmpty().not() && title.isNullOrEmpty().not()) {
                    genres.add(Genre(href, title!!))
                }
            }
            return UIData.success(genres)
        } catch (e: Exception) {
            if (genres.isEmpty().not()) {
                return UIData.success(genres)
            }
            return UIData.errorMessage(e.message)
        }
    }

    override suspend fun loadMangaGenre(genreId: String, filter: MangaFilter): UIData<List<Manga>> {
        val result = api.request(genreId.dropLast(1) + resolveFilter(filter)).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        return loadMangasFromHtml(result.body)
    }

    override suspend fun loadMangaGenre(genreId: String, filter: MangaFilter, offset: Int): UIData<List<Manga>> {
        val result = api.request("${genreId.dropLast(1)}${resolveFilter(filter)}?offset=$offset").await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        return loadMangasFromHtml(result.body)
    }

    private fun resolveFilter(filter: MangaFilter): String {
        var filterString = when (filter) {
            NONE -> ""
            DATE -> "&n=date"
            FAVORITE -> "&n=fav"
            NAME -> "&n=abc"
            CHAPTER -> "&n=ch"
        }
        if (filter != NONE) {
            filterString += if (filter.asc) "asc" else "desc"
        }
        return filterString
    }

    override fun genrePagingCount() = PAGINATION_COUNT

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        val result = api.request("https://mangachan.me/?do=search&subaction=search&story=$term").await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        return loadMangasFromHtml(result.body)
    }

    private fun loadMangasFromHtml(html: String?): UIData<List<Manga>> {
        val mangas = mutableListOf<Manga>()
        html ?: return UIData.success(mangas)
        try {
            val doc = Jsoup.parse(html)
            val contentElem = doc.getElementsByClass("content_row")
            contentElem?.forEach {
                val title = it.attr("title")

                val mangaImages = it.getElementsByClass("manga_images")[0]
                val images = mangaImages.getElementsByTag("img")
                val imageUrl = images?.getOrNull(0)?.attr("src")

                var description = it
                    .getElementsByClass("tags")
                    .getOrNull(0)
                    ?.children()
                    ?.getOrNull(0)
                    ?.textNodes()
                    ?.getOrNull(0)
                    ?.wholeText
                    ?.trim()
                    ?: ""

                if (description.isEmpty()) {
                    description = it
                        .getElementsByClass("tags")
                        ?.getOrNull(0)
                        ?.textNodes()
                        ?.getOrNull(0)
                        ?.wholeText
                        ?.trim()
                        ?: ""
                }

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

    override suspend fun loadMangaDetails(idManga: String): UIData<MangaDetails> {
        val result = api.request(idManga).await()
        if (result.isSuccess().not()) {
            return UIData.errorThrowable(result.throwable)
        }
        val doc = Jsoup.parse(result.body)

        val chapters = mutableListOf<Chapter>()
        var image: String? = null
        var name: String = ""
        var description: String = ""
        val genres = mutableListOf<String>()
        try {
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
            chapters.reverse()

            val mangaImages = doc.getElementById("manga_images")
            val images = mangaImages.getElementsByTag("img")
            image = images?.getOrNull(0)?.attr("src")

            val nameElement = doc.getElementsByClass("title_top_a")[0]
            name = nameElement.wholeText()

            val descriptionElements = doc.getElementById("description")
            description = descriptionElements.textNodes()[0].wholeText.trim()

            val sideTags = doc.getElementsByClass("sidetag")
            sideTags.filter { it !is TextNode }.forEach {
                val genreTitle = it.text().replace("+", "").replace("-", "").trim()
                if (genreTitle.isNotEmpty()) genres.add(genreTitle)
            }

            val mangaDetails = MangaDetails(idManga, image, name, description, genres, chapters)
            return UIData.success(mangaDetails)
        } catch (e: Exception) {
            if (chapters.isEmpty().not()) {
                val mangaDetails = MangaDetails(idManga, image, name, description, genres, chapters)

                return UIData.success(mangaDetails)
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