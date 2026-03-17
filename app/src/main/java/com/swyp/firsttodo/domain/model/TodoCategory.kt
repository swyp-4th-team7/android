package com.swyp.firsttodo.domain.model

sealed interface TodoCategory {
    val displayName: String
    val api: String
}

enum class TodoChildCategory(
    override val displayName: String,
    override val api: String,
) : TodoCategory {
    STUDY("공부", "STUDY"),
    HOMEWORK("숙제", "HOMEWORK"),
    EXERCISE("운동", "EXERCISE"),
    CLEANING("정리", "CLEANING"),
    READING("독서", "READING"),
    HOUSEWORK("집안일", "HOUSEWORK"),
    CREATIVE_ACTIVITY("창의활동", "CREATIVE_ACTIVITY"),
}

enum class TodoParentCategory(
    override val displayName: String,
    override val api: String,
) : TodoCategory {
    STUDY("공부", "STUDY"),
    HOMEWORK("숙제", "HOMEWORK"),
    EXERCISE("운동", "EXERCISE"),
    CLEANING("정리", "CLEANING"),
    READING("독서", "READING"),
    HOUSEWORK("집안일", "HOUSEWORK"),
    CREATIVE_ACTIVITY("창의활동", "CREATIVE_ACTIVITY"),
}
