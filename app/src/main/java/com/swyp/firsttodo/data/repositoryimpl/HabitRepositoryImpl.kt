package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.HabitDataSource
import com.swyp.firsttodo.data.remote.dto.request.habit.FailedHabitRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.domain.error.HabitError
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.domain.repository.HabitRepository
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
            }.map { }
                .recoverCatching { e ->
                    throw when (e) {
                        is ApiError -> when (e.code) {
                            40020 -> HabitError.TitleEmpty(e.serverMsg)
                            40021 -> HabitError.DurationEmpty(e.serverMsg)
                            else -> e
                        }

                        else -> e
                    }
                }

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
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError -> when (e.code) {
                        40020 -> HabitError.TitleEmpty(e.serverMsg)
                        40021 -> HabitError.DurationEmpty(e.serverMsg)
                        40023 -> HabitError.CompletedEmpty(e.serverMsg)
                        40405 -> HabitError.HabitNotFound(e.serverMsg)
                        else -> e
                    }

                    else -> e
                }
            }

        override suspend fun deleteHabit(habitId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                habitDataSource.deleteHabit(habitId)
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError.NotFound -> HabitError.HabitNotFound(e.serverMsg)
                    else -> e
                }
            }

        override suspend fun getHabits(): Result<List<HabitModel>> =
            apiResponseHandler.safeApiCall {
                habitDataSource.getHabitList()
            }.map { it.toModel() }

        override suspend fun getFailedHabits(): Result<List<HabitModel>> =
            apiResponseHandler.safeApiCall {
                habitDataSource.getFailedHabitList()
            }.map { it.toModel() }

        override suspend fun retryFailedHabit(
            habitId: Long,
            duration: HabitDuration,
            reward: String?,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                habitDataSource.patchFailedHabit(
                    habitId = habitId,
                    request = FailedHabitRequestDto(
                        duration = duration.name,
                        reward = reward,
                    ),
                )
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError -> when (e.code) {
                        40022 -> HabitError.RewardEmpty(e.serverMsg)
                        40405 -> HabitError.HabitNotFound(e.serverMsg)
                        else -> e
                    }

                    else -> e
                }
            }
    }
