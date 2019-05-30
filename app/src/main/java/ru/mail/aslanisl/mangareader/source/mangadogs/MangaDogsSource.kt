package ru.mail.aslanisl.mangareader.source.mangadogs

import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.MangaDetails
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource

private const val BASE_URL = "https://api2.niadd.com/"

class MangaDogsSource : IMangaSource {

    private val api by lazy { ApiBuilder().createRetrofit(BASE_URL).create(MangaDogsApi::class.java) }

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadGenres(): UIData<List<Genre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaGenre(genreId: String): UIData<List<Manga>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaGenre(genreId: String, offset: Int): UIData<List<Manga>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun genrePagingCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaDetails(idManga: String): UIData<MangaDetails> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadPages(idChapter: String): UIData<List<Page>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}