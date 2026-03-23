package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.family.ConnectedFamilyMemberDto
import com.swyp.firsttodo.data.remote.dto.response.family.ConnectedFamilyResponseDto
import com.swyp.firsttodo.data.remote.dto.response.family.FamilyDashboardResponseDto
import com.swyp.firsttodo.data.remote.dto.response.family.FamilyMemberDto
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.domain.model.family.FamilyHabit
import com.swyp.firsttodo.domain.model.family.FamilyInfo
import com.swyp.firsttodo.domain.model.family.FamilyTodo

fun FamilyMemberDto.toModel(): FamilyInfo =
    FamilyInfo(
        userId = userId,
        nickname = nickname,
        todo = FamilyTodo(
            totalCount = todo.totalCount,
            completedCount = todo.completedCount,
        ),
        habit = FamilyHabit(
            completed = habit.completed,
        ),
    )

fun FamilyDashboardResponseDto.toModel(): List<FamilyInfo> = members.map { it.toModel() }

fun ConnectedFamilyMemberDto.toModel(): ConnectedFamilyModel =
    ConnectedFamilyModel(
        userId = userId,
        nickname = nickname,
    )

fun ConnectedFamilyResponseDto.toModel(): List<ConnectedFamilyModel> = members.map { it.toModel() }
