package ru.mail.aslanisl.mangareader.data.base

import ru.mail.aslanisl.mangareader.data.base.UIDataStatus.FAILURE
import ru.mail.aslanisl.mangareader.data.base.UIDataStatus.LOADING
import ru.mail.aslanisl.mangareader.data.base.UIDataStatus.SUCCESS

class UIData<out T> private constructor(
    val status: UIDataStatus,
    val body: T? = null,
    var throwable: Throwable? = null,
    val code: Int? = null
) {

    companion object {
        fun loading() = UIData(LOADING, null)

        fun <T> create(apiResponse: ApiResponse<T>): UIData<T> {
            if (apiResponse.isSuccess()) {
                return success(apiResponse.body, apiResponse.throwable)
            }
            return errorThrowable(apiResponse.throwable, apiResponse.code)
        }

        fun <T> loading(cache: T?) = UIData(LOADING, cache)

        fun <T> success(body: T?, throwable: Throwable? = null) = UIData(SUCCESS, body, throwable)

        fun errorThrowable(throwable: Throwable? = null, code: Int? = null): UIData<Nothing> {
            return UIData(
                FAILURE,
                null,
                throwable = throwable,
                code = code
            )
        }

        fun errorMessage(errorMessage: String? = null, code: Int? = null): UIData<Nothing> {
            return UIData(
                status = FAILURE,
                body = null,
                throwable = Throwable(errorMessage),
                code = code
            )
        }
    }

    fun isSuccess() = status == SUCCESS

    fun isLoading() = status == LOADING

    fun isError() = status == FAILURE
}