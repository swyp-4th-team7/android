package com.swyp.firsttodo.core.network.model

sealed class ApiError(
    override val message: String?,
    override val cause: Throwable? = null,
) : Throwable(message, cause) {
    data class BadRequest(val code: Int?, val serverMsg: String) : ApiError(serverMsg)

    data class Unauthorized(val serverMsg: String) : ApiError(serverMsg)

    data class Forbidden(val serverMsg: String) : ApiError(serverMsg)

    data class NotFound(val serverMsg: String) : ApiError(serverMsg)

    data class Conflict(val serverMsg: String) : ApiError(serverMsg)

    data class ServerError(val serverMsg: String) : ApiError(serverMsg)

    class NetworkConnection : ApiError("Network connection failed")

    data class Unknown(
        val errorMsg: String? = null,
        val throwable: Throwable? = null,
    ) : ApiError(errorMsg, throwable)
}
