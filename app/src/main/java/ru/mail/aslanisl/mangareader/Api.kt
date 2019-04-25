package ru.mail.aslanisl.mangareader

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET("search/ajax/")
    fun search(@Query("term") term: String): Call<String>

    @GET("manga/{name}.html")
    fun load(@Path("name") name: String): Call<String>
//
//    @POST("wp-admin/admin-ajax.php?v=8.7.4")
//    @FormUrlEncoded
//    fun search(@Field("td_string") term: String, @Field("action") action: String = "td_ajax_search"): Call<String>

    @GET
    fun loadChapter(@Url href: String): Call<String>
}