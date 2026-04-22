package com.swyp.firsttodo.presentation.main.bottombar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.habit.navigation.HabitRoute
import com.swyp.firsttodo.presentation.reward.navigation.RewardRoute
import com.swyp.firsttodo.presentation.todo.navigation.TodoRoute

enum class MainTab(
    @param:DrawableRes val selectedIconRes: Int,
    @param:DrawableRes val defaultIconRes: Int,
    @param:StringRes val label: Int,
    val route: Route,
) {
    TODO(
        selectedIconRes = R.drawable.ic_todo_selected,
        defaultIconRes = R.drawable.ic_todo_default,
        label = R.string.bottom_nav_todo,
        route = TodoRoute.Todo,
    ),
    HABIT(
        selectedIconRes = R.drawable.ic_habit_selected,
        defaultIconRes = R.drawable.ic_habit_default,
        label = R.string.bottom_nav_habbit,
        route = HabitRoute.Habit,
    ),
    REWARD(
        selectedIconRes = R.drawable.ic_reward_selected,
        defaultIconRes = R.drawable.ic_reward_default,
        label = R.string.bottom_nav_reward,
        route = RewardRoute.Reward,
    ),
}
