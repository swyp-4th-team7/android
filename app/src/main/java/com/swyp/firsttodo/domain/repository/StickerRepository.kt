package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel

interface StickerRepository {
    suspend fun getWeeklyStickers(weekOffset: Int): Result<WeeklyStickersModel>
}
