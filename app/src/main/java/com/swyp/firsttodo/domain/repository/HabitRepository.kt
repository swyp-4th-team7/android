package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel

interface HabitRepository {
    suspend fun createHabit(
        title: String,
        duration: HabitDuration,
        reward: String?,
    ): Result<Unit>

    suspend fun editHabit(
        habitId: Long,
        title: String,
        duration: HabitDuration,
        reward: String?,
        isCompleted: Boolean,
    ): Result<Unit>

    suspend fun deleteHabit(habitId: Long): Result<Unit>

    suspend fun getHabits(): Result<List<HabitModel>>
}
