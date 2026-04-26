package com.swyp.firsttodo.core.analytics

sealed class AnalyticsEvent(
    val name: String,
    val screenName: String? = null,
    val properties: Map<String, Any?>? = null,
)
