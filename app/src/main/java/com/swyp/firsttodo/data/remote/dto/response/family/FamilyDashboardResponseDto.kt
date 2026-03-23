package com.swyp.firsttodo.data.remote.dto.response.family

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FamilyDashboardResponseDto(
    @SerialName("members")
    val members: List<FamilyMemberDto>,
)

@Serializable
data class FamilyMemberDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("todo")
    val todo: FamilyMemberTodoDto,
    @SerialName("habit")
    val habit: FamilyMemberHabitDto,
)

@Serializable
data class FamilyMemberTodoDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("completedCount")
    val completedCount: Int,
)

@Serializable
data class FamilyMemberHabitDto(
    @SerialName("completed")
    val completed: Boolean,
)
