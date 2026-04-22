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
            }.map { }
                .recoverCatching { e ->
                    throw when (e) {
                        is ApiError -> when (e.code) {
                            40014 -> ScheduleError.TitleEmpty(e.serverMsg)
                            40016 -> ScheduleError.CategoryEmpty(e.serverMsg)
                            40017 -> ScheduleError.DateEmpty(e.serverMsg)
                            else -> e
                        }

                        else -> e
                    }
                }

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
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError.NotFound -> ScheduleError.ScheduleNotFound(e.serverMsg)
                    else -> e
                }
            }

        override suspend fun deleteSchedule(scheduleId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                scheduleDataSource.deleteSchedule(scheduleId)
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError.NotFound -> ScheduleError.ScheduleNotFound(e.serverMsg)
                    else -> e
                }
            }
    }
