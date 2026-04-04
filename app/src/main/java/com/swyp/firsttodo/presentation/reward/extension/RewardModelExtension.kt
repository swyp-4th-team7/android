package com.swyp.firsttodo.presentation.reward.extension

import com.swyp.firsttodo.R
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.reward.RewardModel
import com.swyp.firsttodo.domain.model.reward.RewardStatus

val RewardModel.durationIconRes: Int
    get() {
        val completed = this.status == RewardStatus.COMPLETE

        return when (this.duration) {
            HabitDuration.THREE_DAYS -> {
                if (completed) R.drawable.ic_habit_day_3_completed else R.drawable.ic_habit_day_3
            }
            HabitDuration.SEVEN_DAYS -> {
                if (completed) R.drawable.ic_habit_day_7_completed else R.drawable.ic_habit_day_7
            }
            HabitDuration.FOURTEEN_DAYS -> {
                if (completed) R.drawable.ic_habit_day_14_completed else R.drawable.ic_habit_day_14
            }
            HabitDuration.TWENTYONE_DAYS -> {
                if (completed) R.drawable.ic_habit_day_21_completed else R.drawable.ic_habit_day_21
            }
            HabitDuration.SIXTYSIX_DAYS -> {
                if (completed) R.drawable.ic_habit_day_66_completed else R.drawable.ic_habit_day_66
            }
            HabitDuration.NINETYNINE_DAYS -> {
                if (completed) R.drawable.ic_habit_day_99_completed else R.drawable.ic_habit_day_99
            }
        }
    }
