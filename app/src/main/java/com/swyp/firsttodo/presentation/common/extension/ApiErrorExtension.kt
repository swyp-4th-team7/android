package com.swyp.firsttodo.presentation.common.extension

import com.swyp.firsttodo.core.network.model.ApiError

fun ApiError.snackbarMsg(): String =
    when (this) {
        is ApiError.BadRequest -> serverMsg
        is ApiError.Unauthorized -> "로그인이 필요해요. 다시 로그인해주세요."
        is ApiError.Forbidden -> "접근 권한이 없어요."
        is ApiError.NotFound -> serverMsg
        is ApiError.Conflict -> serverMsg
        is ApiError.ServerError -> "알 수 없는 에러가 발생했어요. 문제 상황이 지속되면 고객센터로 알려주세요."
        is ApiError.NetworkConnection -> "네트워크 상태가 불안정해요. 다시 시도해주세요."
        is ApiError.Unknown -> "알 수 없는 에러가 발생했어요. 문제 상황이 지속되면 고객센터로 알려주세요."
    }
