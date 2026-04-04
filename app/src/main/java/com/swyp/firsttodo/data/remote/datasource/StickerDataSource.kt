package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.sticker.ChildrenStickerResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerBoardResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto

interface StickerDataSource {
    suspend fun getWeeklyStickers(weekOffset: Int): BaseResponse<StickerListResponseDto>

    suspend fun getStickerBoard(): BaseResponse<StickerBoardResponseDto>

    suspend fun postStickerPopupConfirm(): BaseResponse<Unit>

    suspend fun getChildrenStickerList(): BaseResponse<ChildrenStickerResponseDto>
}
