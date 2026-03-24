package com.swyp.firsttodo.domain.model.sticker

data class WeeklyStickersModel(
    val weekLabel: String,
    val weekOffset: Int,
    val startDate: String,
    val endDate: String,
    val stickers: List<WeeklyStickerModel>,
)

data class WeeklyStickerModel(
    val date: String,
    val stickerCode: String?,
)
