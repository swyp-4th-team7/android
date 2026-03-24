package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.sticker.ChildrenStickerResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerBoardResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StickerService {
    @GET("/api/v1/stickers/weekly")
    suspend fun getWeeklyStickers(
        @Query("weekOffset") weekOffset: Int,
    ): BaseResponse<StickerListResponseDto>

    @GET("/api/v1/stickers/board")
    suspend fun getStickerBoard(): BaseResponse<StickerBoardResponseDto>

    @POST("/api/v1/stickers/board/confirm")
    suspend fun postStickerPopupConfirm(): BaseResponse<Unit>

    @GET("/api/v1/stickers/children")
    suspend fun getChildrenStickerList(): BaseResponse<ChildrenStickerResponseDto>
}
