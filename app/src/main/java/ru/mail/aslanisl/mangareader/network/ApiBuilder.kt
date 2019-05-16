package ru.mail.aslanisl.mangareader.network

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.mail.aslanisl.mangareader.App
import ru.mail.aslanisl.mangareader.BuildConfig
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 60L

class ApiBuilder {

    fun createRetrofit(baseUrl: String): Retrofit {
        val okClientBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okClientBuilder.addInterceptor(ChuckInterceptor(App.instance))
        }

        val builder = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(DeferredApiFactory.create())
            .baseUrl(baseUrl)
            .client(okClientBuilder.build())

        return builder.build()
    }
}