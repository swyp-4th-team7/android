package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.ScheduleDataSource
import com.swyp.firsttodo.data.remote.dto.request.schedule.ScheduleRequestDto
import com.swyp.firsttodo.domain.model.ScheduleModel
import com.swyp.firsttodo.domain.repository.ScheduleRepository
import com.swyp.firsttodo.domain.throwable.ScheduleError
import javax.inject.Inject
import kotlin.map

class ScheduleRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val scheduleDataSource: ScheduleDataSource,
    ) : ScheduleRepository {
        override suspend fun createSchedule(
            title: String,
            category: String,
            scheduleDate: String,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                scheduleDataSource.createSchedule(ScheduleRequestDto(title, category, scheduleDate))
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.BadRequest) {
                        when (throwable.code) {
                            40014 -> ScheduleError.TitleEmpty(throwable.serverMsg)
                            40016 -> ScheduleError.CategoryEmpty(throwable.serverMsg)
                            40017 -> ScheduleError.DateEmpty(throwable.serverMsg)
                            else -> throwable
                        }
                    } else {
                        throwable
                    }

                    Result.failure(error)
                },
            )

        override suspend fun getSchedules(): Result<List<ScheduleModel>> =
            apiResponseHandler.safeApiCall {
                scheduleDataSource.getSchedules()
            }.map { list -> list.map { it.toModel() } }

        override suspend fun updateSchedule(
            scheduleId: Long,
            title: String,
            category: String,
            scheduleDate: String,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                scheduleDataSource.patchSchedule(scheduleId, ScheduleRequestDto(title, category, scheduleDate))
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.NotFound) {
                        ScheduleError.ScheduleNotFound(throwable.serverMsg)
                    } else {
                        throwable
                    }

                    Result.failure(error)
                },
            )

        override suspend fun deleteSchedule(scheduleId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                scheduleDataSource.deleteSchedule(scheduleId)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.NotFound) {
                        ScheduleError.ScheduleNotFound(throwable.serverMsg)
                    } else {
                        throwable
                    }

                    Result.failure(error)
                },
            )
    }
