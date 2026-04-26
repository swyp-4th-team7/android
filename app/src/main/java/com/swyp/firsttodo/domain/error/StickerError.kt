package com.swyp.firsttodo.domain.error

sealed class StickerError(
    override val message: String?,
) : Throwable(message) {
    class WeekOffsetInvalid(message: String) : StickerError(message) // weekOffset이 -52 ~ 52 사이가 아님
}
