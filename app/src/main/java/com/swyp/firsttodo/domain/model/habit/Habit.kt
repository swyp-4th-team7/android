package com.swyp.firsttodo.domain.model.habit

data class Habit(
    val habitId: Long,
    val duration: HabitDuration,
    val isCompleted: Boolean,
    val title: String,
    val reward: String,
)

enum class HabitDuration {
    THREE_DAYS,
    SEVEN_DAYS,
    FOURTEEN_DAYS,
    TWENTYONE_DAYS,
    SIXTYSIX_DAYS,
    NINETYNINE_DAYS,
}
