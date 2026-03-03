package com.swyp.firsttodo.core.network.util

import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.core.network.model.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiResponseHandler
    @Inject
    constructor(
        private val json: Json,
    ) {
        suspend fun <T> safeApiCall(block: suspend () -> BaseResponse<T>): Result<T> =
            suspendRunCatching {
                val response = block()
                handleResponse(response)
            }.recoverCatching { throwable ->
                throw when (throwable) {
                    is HttpException -> parseHttpException(throwable)
                    is UnknownHostException, is SocketTimeoutException -> ApiError.NetworkConnection()
                    is ApiError -> throwable
                    else -> ApiError.Unknown(throwable.message)
                }
            }

        private fun <T> handleResponse(response: BaseResponse<T>): T {
            val code = response.code
            val message = response.message

            if (code in 20000..20499) {
                if (response.data == null) {
                    @Suppress("UNCHECKED_CAST")
                    return (Unit as T) ?: throw ApiError.ServerError("Success code but data is null")
                }
                return response.data
            }

            throw when (code) {
                in 40000..40099 -> ApiError.BadRequest(code, message)
                in 40100..40199 -> ApiError.Unauthorized(message)
                in 40300..40399 -> ApiError.Forbidden(message)
                in 40400..40499 -> ApiError.NotFound(message)
                in 40900..40999 -> ApiError.Conflict(message)
                in 50000..59999 -> ApiError.ServerError(message)
                else -> ApiError.Unknown(message)
            }
        }

        private fun parseHttpException(e: HttpException): ApiError {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = runCatching {
                errorBody?.let { json.decodeFromString<ErrorResponse>(it) }
            }.getOrNull()

            val serverCode = errorResponse?.code
            val serverMsg = errorResponse?.message ?: e.message()

            return when (e.code()) {
                400 -> ApiError.BadRequest(serverCode, serverMsg)
                401 -> ApiError.Unauthorized(serverMsg)
                403 -> ApiError.Forbidden(serverMsg)
                404 -> ApiError.NotFound(serverMsg)
                409 -> ApiError.Conflict(serverMsg)
                in 500..599 -> ApiError.ServerError(serverMsg)
                else -> ApiError.Unknown(serverMsg, e)
            }
        }
    }
