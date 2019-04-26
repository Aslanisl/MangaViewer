package ru.mail.aslanisl.mangareader.source.teenmanga

import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.mail.aslanisl.mangareader.dataModel.base.ApiResponse

interface TeenMangaApi {

    @POST("wp-admin/admin-ajax.php?v=8.7.4")
    @FormUrlEncoded
    fun search(@Field("td_string") term: String, @Field("action") action: String = "td_ajax_search"): Deferred<ApiResponse<String>>

}