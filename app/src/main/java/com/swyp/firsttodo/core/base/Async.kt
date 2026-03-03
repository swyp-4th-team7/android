package com.swyp.firsttodo.core.base

sealed interface Async<out T> {
    data object Empty : Async<Nothing>

    data class Loading<out T>(
        val data: T? = null,
    ) : Async<T>

    data class Success<out T>(
        val data: T,
    ) : Async<T>

    data class Failure(
        val message: String,
    ) : Async<Nothing>

    data object Init : Async<Nothing>
}
