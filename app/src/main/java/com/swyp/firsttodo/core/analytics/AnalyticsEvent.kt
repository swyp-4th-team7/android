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
            properties = mapOf<String, Any>(
                "method" to method.lowercase(),
                "is_profile_completed" to isProfileCompleted,
            ),
        )

    private object Screen {
        const val LOGIN = "login"
    }
}
