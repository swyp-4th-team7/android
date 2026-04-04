package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.sticker.ChildStickerModel
import com.swyp.firsttodo.domain.model.sticker.StickerBoardModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel

interface StickerRepository {
    suspend fun getWeeklyStickers(weekOffset: Int): Result<WeeklyStickersModel>

    suspend fun getStickerBoard(): Result<StickerBoardModel>

    suspend fun stickerPopupConfirm(): Result<Unit>

    suspend fun getChildrenStickerList(): Result<List<ChildStickerModel>>
}
