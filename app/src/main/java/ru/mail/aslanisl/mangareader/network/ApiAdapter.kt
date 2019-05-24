package ru.mail.aslanisl.mangareader.network

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ru.mail.aslanisl.mangareader.data.base.ApiResponse
import java.lang.reflect.Type

class ApiAdapter<T>(private val responseType: Type) : CallAdapter<T, Deferred<ApiResponse<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): Deferred<ApiResponse<T>> {
        val deferred = CompletableDeferred<ApiResponse<T>>()

        deferred.invokeOnCompletion {
            if (deferred.isCancelled) {
                call.cancel()
            }
        }

        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                deferred.complete(ApiResponse.create(t))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                deferred.complete(ApiResponse.create(response))
            }
        })

        return deferred
    }
}