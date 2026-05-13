package com.swyp.firsttodo.core.common.extension

import com.swyp.firsttodo.core.network.model.ApiError

fun ApiError.snackbarMsg(): String =
    when (this) {
        is ApiError.BadRequest -> serverMsg
        is ApiError.Unauthorized -> "로그인이 만료되었어요. 다시 로그인해 주세요."
        is ApiError.Forbidden -> "접근 권한이 없어요."
        is ApiError.NotFound -> serverMsg
        is ApiError.Conflict -> serverMsg
        is ApiError.ServerError -> "오류가 발생했어요!"
        is ApiError.NetworkConnection -> "네트워크 상태가 불안정해요. 다시 시도해 주세요."
        is ApiError.Unknown -> "오류가 발생했어요!"
    }
