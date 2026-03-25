package com.swyp.firsttodo.core.network.model

sealed class ApiError(
    open val code: Int?,
    override val message: String?,
    override val cause: Throwable? = null,
) : Throwable(message, cause) {
    open val serverMsg: String get() = message ?: ""

    data class BadRequest(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    data class Unauthorized(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    data class Forbidden(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    data class NotFound(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    data class Conflict(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    data class ServerError(override val code: Int?, override val serverMsg: String) : ApiError(code, serverMsg)

    class NetworkConnection : ApiError(null, "Network connection failed")

    data class Unknown(
        val errorMsg: String? = null,
        val throwable: Throwable? = null,
    ) : ApiError(null, errorMsg, throwable)
}
