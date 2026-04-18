package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.habit.FailedHabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.FailedHabitResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel

private fun String.toHabitDuration(): HabitDuration =
    runCatching { HabitDuration.valueOf(this) }.getOrDefault(HabitDuration.SEVEN_DAYS)

fun HabitResponseDto.toModel(): HabitModel =
    HabitModel(
        habitId = habitId,
        title = title,
        duration = duration.toHabitDuration(),
        reward = reward,
        isCompleted = isCompleted,
    )

fun FailedHabitResponseDto.toModel(): HabitModel =
    HabitModel(
        habitId = habitId,
        title = title,
        duration = duration.toHabitDuration(),
        reward = reward,
        isCompleted = true,
    )

fun HabitListResponseDto.toModel(): List<HabitModel> = habits.map { it.toModel() }

fun FailedHabitListResponseDto.toModel(): List<HabitModel> = failedHabits.map { it.toModel() }
