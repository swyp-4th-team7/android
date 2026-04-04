package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.StickerDataSource
import com.swyp.firsttodo.domain.model.sticker.ChildStickerModel
import com.swyp.firsttodo.domain.model.sticker.StickerBoardModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import com.swyp.firsttodo.domain.repository.StickerRepository
import com.swyp.firsttodo.domain.throwable.StickerError
import javax.inject.Inject

class StickerRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val stickerDataSource: StickerDataSource,
    ) : StickerRepository {
        override suspend fun getWeeklyStickers(weekOffset: Int): Result<WeeklyStickersModel> =
            apiResponseHandler.safeApiCall {
                stickerDataSource.getWeeklyStickers(weekOffset)
            }.map { it.toModel() }
                .recoverCatching { throwable ->
                    throw if (throwable is ApiError.BadRequest && throwable.code == 40024) {
                        StickerError.WeekOffsetInvalid(throwable.serverMsg)
                    } else {
                        throwable
                    }
                }

        override suspend fun getStickerBoard(): Result<StickerBoardModel> =
            apiResponseHandler.safeApiCall {
                stickerDataSource.getStickerBoard()
            }.map { it.toModel() }

        override suspend fun stickerPopupConfirm(): Result<Unit> =
            apiResponseHandler.safeApiCall {
                stickerDataSource.postStickerPopupConfirm()
            }

        override suspend fun getChildrenStickerList(): Result<List<ChildStickerModel>> =
            apiResponseHandler.safeApiCall {
                stickerDataSource.getChildrenStickerList()
            }.map { it.toModel() }
    }
