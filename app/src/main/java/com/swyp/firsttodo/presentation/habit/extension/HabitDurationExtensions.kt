package com.swyp.firsttodo.presentation.habit.extension

import com.swyp.firsttodo.domain.model.habit.HabitDuration

val HabitDuration.displayName: String
    get() = when (this) {
        HabitDuration.THREE_DAYS -> "3일"
        HabitDuration.SEVEN_DAYS -> "7일"
        HabitDuration.FOURTEEN_DAYS -> "14일"
        HabitDuration.TWENTYONE_DAYS -> "21일"
        HabitDuration.SIXTYSIX_DAYS -> "66일"
        HabitDuration.NINETYNINE_DAYS -> "99일"
    }
