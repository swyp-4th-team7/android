package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto

interface StickerDataSource {
    suspend fun getWeeklyStickers(weekOffset: Int): BaseResponse<StickerListResponseDto>
}
