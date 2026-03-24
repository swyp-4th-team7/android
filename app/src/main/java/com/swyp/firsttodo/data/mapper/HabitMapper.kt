package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseListDto
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

fun HabitResponseListDto.toModel(): List<HabitModel> = habits.map { it.toModel() }
