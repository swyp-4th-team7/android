package com.swyp.firsttodo.domain.model.habit

data class HabitModel(
    val habitId: Long,
    val title: String,
    val duration: HabitDuration,
    val reward: String?,
    val isCompleted: Boolean,
)
