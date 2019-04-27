package ru.mail.aslanisl.mangareader.source.ninemanga

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import ru.mail.aslanisl.mangareader.data.base.ApiResponse

interface NineMangaApi {

    @GET("search/ajax/")
    fun search(@Query("term") term: String): Deferred<ApiResponse<String>>

    @GET("manga/{name}.html")
    fun load(@Path("name") name: String): Deferred<ApiResponse<String>>

    @GET
    fun loadChapter(@Url href: String): Deferred<ApiResponse<String>>
}