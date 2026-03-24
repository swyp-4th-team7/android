package com.swyp.firsttodo.domain.model.sticker

data class ChildStickerModel(
    val childId: Long,
    val nickname: String?,
    val boardNumber: Int,
    val filledSlots: Int,
    val boardSize: Int,
    val startDate: String?,
)
