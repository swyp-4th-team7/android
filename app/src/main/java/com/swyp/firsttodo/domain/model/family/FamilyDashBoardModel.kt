package com.swyp.firsttodo.domain.model.family

data class FamilyInfo(
    val userId: Long,
    val nickname: String,
    val todo: FamilyTodo,
    val habit: FamilyHabit,
)

data class FamilyTodo(
    val totalCount: Int,
    val completedCount: Int,
)

data class FamilyHabit(
    val completed: Boolean,
)
