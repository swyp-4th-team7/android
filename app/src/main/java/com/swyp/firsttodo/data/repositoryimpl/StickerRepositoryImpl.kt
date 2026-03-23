package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.StickerDataSource
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
            }.fold(
                onSuccess = { Result.success(it.toModel()) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.BadRequest && throwable.code == 40024) {
                        StickerError.WeekOffsetInvalid(throwable.serverMsg)
                    } else {
                        throwable
                    }
                    Result.failure(error)
                },
            )
    }
