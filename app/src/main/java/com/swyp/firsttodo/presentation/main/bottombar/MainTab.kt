package com.swyp.firsttodo.presentation.main.bottombar

import androidx.annotation.DrawableRes
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.growth.navigation.GrowthRoute
import com.swyp.firsttodo.presentation.habit.navigation.HabitRoute
import com.swyp.firsttodo.presentation.reward.navigation.RewardRoute
import com.swyp.firsttodo.presentation.todo.navigation.TodoRoute

enum class MainTab(
    @param:DrawableRes val selectedIconRes: Int,
    @param:DrawableRes val defaultIconRes: Int,
    val label: String,
    val route: Route,
) {
    TODO(
        selectedIconRes = R.drawable.ic_todo_selected,
        defaultIconRes = R.drawable.ic_todo_default,
        label = "할 일",
        route = TodoRoute.Todo,
    ),
    HABIT(
        selectedIconRes = R.drawable.ic_habit_selected,
        defaultIconRes = R.drawable.ic_habit_default,
        label = "습관",
        route = HabitRoute.Habit,
    ),
    REWARD(
        selectedIconRes = R.drawable.ic_reward_selected,
        defaultIconRes = R.drawable.ic_reward_default,
        label = "보상",
        route = RewardRoute.Reward,
    ),
    GROWTH(
        selectedIconRes = R.drawable.ic_growth_selected,
        defaultIconRes = R.drawable.ic_growth_default,
        label = "성장",
        route = GrowthRoute.Growth,
    ),
}
