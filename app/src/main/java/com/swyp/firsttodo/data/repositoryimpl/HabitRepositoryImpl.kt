package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.HabitDataSource
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.domain.repository.HabitRepository
import com.swyp.firsttodo.domain.throwable.HabitError
import javax.inject.Inject

class HabitRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val habitDataSource: HabitDataSource,
    ) : HabitRepository {
        override suspend fun createHabit(
            title: String,
            duration: HabitDuration,
            reward: String?,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                habitDataSource.postHabit(HabitPostRequestDto(title, duration.name, reward))
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.BadRequest) {
                        when (throwable.code) {
                            40020 -> HabitError.TitleEmpty(throwable.serverMsg)
                            40021 -> HabitError.DurationEmpty(throwable.serverMsg)
                            else -> throwable
                        }
                    } else {
                        throwable
                    }
                    Result.failure(error)
                },
            )

        override suspend fun editHabit(
            habitId: Long,
            title: String,
            duration: HabitDuration,
            reward: String?,
            isCompleted: Boolean,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                habitDataSource.patchHabit(
                    habitId = habitId,
                    request = HabitPatchRequestDto(title, duration.name, reward, isCompleted),
                )
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = when (throwable) {
                        is ApiError.BadRequest -> {
                            when (throwable.code) {
                                40020 -> HabitError.TitleEmpty(throwable.serverMsg)
                                40021 -> HabitError.DurationEmpty(throwable.serverMsg)
                                40023 -> HabitError.CompletedEmpty(throwable.serverMsg)
                                else -> throwable
                            }
                        }

                        is ApiError.NotFound -> HabitError.HabitNotFound(throwable.serverMsg)

                        else -> throwable
                    }
                    Result.failure(error)
                },
            )

        override suspend fun deleteHabit(habitId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                habitDataSource.deleteHabit(habitId)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = when (throwable) {
                        is ApiError.NotFound -> HabitError.HabitNotFound(throwable.serverMsg)
                        else -> throwable
                    }
                    Result.failure(error)
                },
            )

        override suspend fun getHabits(): Result<List<HabitModel>> =
            apiResponseHandler.safeApiCall {
                habitDataSource.getHabitList()
            }.map { it.toModel() }
    }
