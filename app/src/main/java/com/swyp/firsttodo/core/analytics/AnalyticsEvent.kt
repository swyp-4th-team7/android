package com.swyp.firsttodo.core.analytics

sealed class AnalyticsEvent(
    val name: String,
    val screenName: String? = null,
    val properties: Map<String, Any?>? = null,
) {
    data class Login(
        val method: String,
        val isProfileCompleted: Boolean,
    ) : AnalyticsEvent(
            name = "login",
            screenName = Screen.LOGIN,
            properties = mapOf(
                "method" to method.lowercase(),
                "is_profile_completed" to isProfileCompleted,
            ),
        )

    data class CompleteProfile(
        val role: String,
    ) : AnalyticsEvent(
            name = "complete_profile",
            screenName = Screen.ONBOARDING,
            properties = mapOf(
                "role" to role.lowercase(),
            ),
        )

    data object Logout : AnalyticsEvent(
        name = "logout",
        screenName = Screen.SIDE_MENU,
    )

    data object Withdraw : AnalyticsEvent(
        name = "withdraw",
        screenName = Screen.SIDE_MENU,
    )

    data object ConnectFamily : AnalyticsEvent(
        name = "connect_family",
        screenName = Screen.SHARE,
    )

    data object DisconnectFamily : AnalyticsEvent(
        name = "disconnect_family",
        screenName = Screen.SHARE,
    )

    data class CreateTodo(
        val category: String,
    ) : AnalyticsEvent(
            name = "create_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "category" to category,
            ),
        )

    data class EditTodo(
        val todoId: Long,
        val category: String,
    ) : AnalyticsEvent(
            name = "edit_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "todo_id" to todoId.toString(),
                "category" to category,
            ),
        )

    data class DeleteTodo(
        val todoId: Long,
    ) : AnalyticsEvent(
            name = "delete_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "todo_id" to todoId.toString(),
            ),
        )

    data class ToggleTodo(
        val todoId: Long,
        val isChecked: Boolean,
    ) : AnalyticsEvent(
            name = "toggle_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "todo_id" to todoId.toString(),
                "is_checked" to isChecked,
            ),
        )

    data class CreateSchedule(
        val date: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "create_schedule",
            screenName = Screen.TODO,
            properties = mapOf(
                "date" to date,
                "category" to category,
            ),
        )

    data class EditSchedule(
        val scheduleId: Long,
        val date: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "edit_schedule",
            screenName = Screen.TODO,
            properties = mapOf(
                "schedule_id" to scheduleId.toString(),
                "date" to date,
                "category" to category,
            ),
        )

    data class DeleteSchedule(
        val scheduleId: Long,
    ) : AnalyticsEvent(
            name = "delete_schedule",
            screenName = Screen.TODO,
            properties = mapOf(
                "schedule_id" to scheduleId.toString(),
            ),
        )

    data class CreateHabit(
        val duration: String,
    ) : AnalyticsEvent(
            name = "create_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "duration" to duration.lowercase(),
            ),
        )

    data class EditHabit(
        val habitId: Long,
        val duration: String,
    ) : AnalyticsEvent(
            name = "edit_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "duration" to duration.lowercase(),
            ),
        )

    data class DeleteHabit(
        val habitId: Long,
    ) : AnalyticsEvent(
            name = "delete_habit",
            screenName = Screen.HABIT_LIST,
            properties = mapOf(
                "habit_id" to habitId.toString(),
            ),
        )

    data class ToggleHabit(
        val habitId: Long,
        val isChecked: Boolean,
    ) : AnalyticsEvent(
            name = "toggle_habit",
            screenName = Screen.HABIT_LIST,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "is_checked" to isChecked,
            ),
        )

    data class DeleteFailedHabit(
        val habitId: Long,
    ) : AnalyticsEvent(
            name = "delete_failed_habit",
            screenName = Screen.HABIT_LIST,
            properties = mapOf(
                "habit_id" to habitId.toString(),
            ),
        )

    data class RetryHabit(
        val habitId: Long,
        val duration: String,
    ) : AnalyticsEvent(
            name = "retry_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "duration" to duration.lowercase(),
            ),
        )

    data class AcceptReward(
        val habitId: Long,
        val duration: String,
    ) : AnalyticsEvent(
            name = "accept_reward",
            screenName = Screen.REWARD_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "duration" to duration.lowercase(),
            ),
        )

    data class GiveReward(
        val habitId: Long,
        val duration: String,
    ) : AnalyticsEvent(
            name = "give_reward",
            screenName = Screen.REWARD_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "duration" to duration.lowercase(),
            ),
        )

    private object Screen {
        const val LOGIN = "login"
        const val ONBOARDING = "onboarding"
        const val SIDE_MENU = "side_menu"
        const val SHARE = "share"
        const val TODO = "todo"
        const val HABIT_DETAIL = "habit_detail"
        const val HABIT_LIST = "habit_list"
        const val REWARD_DETAIL = "reward_detail"
    }
}
