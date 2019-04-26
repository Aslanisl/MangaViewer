package ru.mail.aslanisl.mangareader.source.mangachan

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url
import ru.mail.aslanisl.mangareader.dataModel.base.ApiResponse

interface MangaChanApi {
    @GET
    fun request(@Url url: String): Deferred<ApiResponse<String>>
}