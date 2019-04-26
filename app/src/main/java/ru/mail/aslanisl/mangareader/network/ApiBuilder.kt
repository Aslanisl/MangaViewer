package ru.mail.aslanisl.mangareader.network

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.mail.aslanisl.mangareader.App
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 60L

class ApiBuilder {

    fun createRetrofit(baseUrl: String): Retrofit {
        val okClient = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(App.instance))
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build();

        val builder = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(DeferredApiFactory.create())
            .baseUrl(baseUrl)
            .client(okClient)

        return builder.build()
    }
}