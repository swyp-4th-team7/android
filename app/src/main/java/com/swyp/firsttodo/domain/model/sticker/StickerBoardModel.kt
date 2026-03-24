package com.swyp.firsttodo.domain.model.sticker

data class StickerBoardModel(
    val boardSize: Int,
    val filledSlotCount: Int,
    val showCompletionPopup: Boolean,
)
