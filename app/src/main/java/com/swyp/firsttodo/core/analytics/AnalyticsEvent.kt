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
        val nickname: String,
    ) : AnalyticsEvent(
            name = "complete_profile",
            screenName = Screen.ONBOARDING,
            properties = mapOf(
                "role" to role.lowercase(),
                "nickname" to nickname,
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

    data class ConnectFamily(
        val code: String,
    ) : AnalyticsEvent(
            name = "connect_family",
            screenName = Screen.SHARE,
            properties = mapOf(
                "code" to code,
            ),
        )

    data class DisconnectFamily(
        val nickname: String,
    ) : AnalyticsEvent(
            name = "disconnect_family",
            screenName = Screen.SHARE,
            properties = mapOf(
                "nickname" to nickname,
            ),
        )

    data class CreateTodo(
        val title: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "create_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "title" to title,
                "category" to category,
            ),
        )

    data class EditTodo(
        val todoId: Long,
        val title: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "edit_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "todo_id" to todoId.toString(),
                "title" to title,
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
        val title: String,
        val isChecked: Boolean,
    ) : AnalyticsEvent(
            name = "toggle_todo",
            screenName = Screen.TODO,
            properties = mapOf(
                "todo_id" to todoId.toString(),
                "title" to title,
                "is_checked" to isChecked,
            ),
        )

    data class CreateSchedule(
        val title: String,
        val date: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "create_schedule",
            screenName = Screen.TODO,
            properties = mapOf(
                "title" to title,
                "date" to date,
                "category" to category,
            ),
        )

    data class EditSchedule(
        val scheduleId: Long,
        val title: String,
        val date: String,
        val category: String,
    ) : AnalyticsEvent(
            name = "edit_schedule",
            screenName = Screen.TODO,
            properties = mapOf(
                "schedule_id" to scheduleId.toString(),
                "title" to title,
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
        val title: String,
        val duration: String,
        val reward: String?,
    ) : AnalyticsEvent(
            name = "create_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "title" to title,
                "duration" to duration.lowercase(),
                "reward" to reward,
            ),
        )

    data class EditHabit(
        val habitId: Long,
        val title: String,
        val duration: String,
        val reward: String?,
    ) : AnalyticsEvent(
            name = "edit_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "title" to title,
                "duration" to duration.lowercase(),
                "reward" to reward,
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
        val title: String,
        val duration: String,
        val reward: String?,
    ) : AnalyticsEvent(
            name = "retry_habit",
            screenName = Screen.HABIT_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "title" to title,
                "duration" to duration.lowercase(),
                "reward" to reward,
            ),
        )

    data class AcceptReward(
        val habitId: Long,
        val title: String,
        val duration: String,
        val reward: String?,
    ) : AnalyticsEvent(
            name = "accept_reward",
            screenName = Screen.REWARD_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "title" to title,
                "duration" to duration.lowercase(),
                "reward" to reward,
            ),
        )

    data class GiveReward(
        val habitId: Long,
        val title: String,
        val duration: String,
        val reward: String?,
    ) : AnalyticsEvent(
            name = "give_reward",
            screenName = Screen.REWARD_DETAIL,
            properties = mapOf(
                "habit_id" to habitId.toString(),
                "title" to title,
                "duration" to duration.lowercase(),
                "reward" to reward,
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
