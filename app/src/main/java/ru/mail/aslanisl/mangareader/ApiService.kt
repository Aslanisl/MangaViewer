package ru.mail.aslanisl.mangareader

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://ru.ninemanga.com/"
const val BASE_URL_WITHOUT_LINE = "http://ru.ninemanga.com"
private const val BASE_URL_PHOTO = "https://ruimg.taadd.com"
private const val TIMEOUT_SECONDS = 60L

object ApiService {

    val api: Api
        get() {
            if (tempApi == null) {
                tempApi = createRetrofit().create(Api::class.java)
            }
            return tempApi as Api
        }

    private var tempApi: Api? = null

    private fun createRetrofit(): Retrofit {
        val okClient = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(App.instance))
            .addInterceptor(UserAgentInterceptor("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0"))
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build();

        val builder = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okClient)

        return builder.build()
    }

    fun convertPhotoUrl(url: String): String {
        return BASE_URL_PHOTO + url
    }

    class UserAgentInterceptor(private val userAgent: String) : Interceptor {

        override fun intercept(chain: Chain): Response {
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build()
            return chain.proceed(requestWithUserAgent)
        }
    }
}