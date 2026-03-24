package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerResponseDto
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickerModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel

fun StickerListResponseDto.toModel(): WeeklyStickersModel =
    WeeklyStickersModel(
        weekLabel = this.weekLabel,
        weekOffset = this.weekOffset,
        startDate = this.startDate,
        endDate = this.endDate,
        stickers = this.stickers.map { it.toModel() },
    )

fun StickerResponseDto.toModel(): WeeklyStickerModel =
    WeeklyStickerModel(
        date = this.date,
        stickerCode = this.stickerCode,
    )
