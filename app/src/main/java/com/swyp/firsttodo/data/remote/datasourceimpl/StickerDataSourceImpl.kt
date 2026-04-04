package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.StickerDataSource
import com.swyp.firsttodo.data.remote.dto.response.sticker.ChildrenStickerResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerBoardResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto
import com.swyp.firsttodo.data.remote.service.StickerService
import javax.inject.Inject

class StickerDataSourceImpl
    @Inject
    constructor(
        private val stickerService: StickerService,
    ) : StickerDataSource {
        override suspend fun getWeeklyStickers(weekOffset: Int): BaseResponse<StickerListResponseDto> =
            stickerService.getWeeklyStickers(weekOffset)

        override suspend fun getStickerBoard(): BaseResponse<StickerBoardResponseDto> = stickerService.getStickerBoard()

        override suspend fun postStickerPopupConfirm(): BaseResponse<Unit> = stickerService.postStickerPopupConfirm()

        override suspend fun getChildrenStickerList(): BaseResponse<ChildrenStickerResponseDto> =
            stickerService.getChildrenStickerList()
    }
