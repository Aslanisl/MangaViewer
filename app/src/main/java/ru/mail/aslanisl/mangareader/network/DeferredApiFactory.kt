package ru.mail.aslanisl.mangareader.network

import kotlinx.coroutines.Deferred
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import ru.mail.aslanisl.mangareader.data.base.ApiResponse
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class DeferredApiFactory private constructor() : CallAdapter.Factory() {
    companion object {
        fun create() = DeferredApiFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Deferred::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException("Deferred return type must be parameterized as Deferred<ApiResponse<*>>")
        }
        val observableType = Factory.getParameterUpperBound(0, returnType)
        val rawObservableType = Factory.getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("Type must be a ApiResponse")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalStateException(
                "ApiResponse must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>")
        }

        return ApiAdapter<Any>(getParameterUpperBound(0, observableType))
    }
}