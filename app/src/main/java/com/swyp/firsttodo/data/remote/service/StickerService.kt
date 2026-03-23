package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StickerService {
    @GET("/api/v1/stickers")
    suspend fun getWeeklyStickers(
        @Query("weekOffset") weekOffset: Int,
    ): BaseResponse<StickerListResponseDto>
}
