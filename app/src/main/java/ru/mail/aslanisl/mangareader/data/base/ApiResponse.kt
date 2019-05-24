package ru.mail.aslanisl.mangareader.data.base

import retrofit2.Response
import retrofit2.Retrofit

class ApiResponse<T>(
    val body: T? = null,
    var throwable: Throwable? = null,
    val code: Int? = null
) {

    companion object {
        fun <T> create(throwable: Throwable): ApiResponse<T> {
            return ApiResponse(throwable = throwable, code = 500)
        }

        fun <T> create(response: Response<T>?): ApiResponse<T> {
            if (response?.isSuccessful == true) {
                return ApiResponse(response.body(), code = 200)
            }
            return ApiResponse(throwable = Throwable(), code = response?.code())
        }
    }

    fun isSuccess() = body != null
}