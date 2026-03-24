package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.StickerDataSource
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
    }
