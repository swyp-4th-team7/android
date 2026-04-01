package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.growth.ChildGrowthResponseDto
import com.swyp.firsttodo.data.remote.dto.response.growth.ChildrenGrowthResponseDto
import com.swyp.firsttodo.data.remote.dto.response.growth.GrowthResponseDto
import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel
import com.swyp.firsttodo.domain.model.growth.GrowthModel

fun GrowthResponseDto.toModel(): GrowthModel =
    GrowthModel(
        starCount = this.starCount,
        weekRange = this.weekRange,
    )

fun ChildrenGrowthResponseDto.toModel(): List<ChildGrowthModel> = this.children.map { it.toModel() }

fun ChildGrowthResponseDto.toModel(): ChildGrowthModel =
    ChildGrowthModel(
        childId = this.childId,
        nickname = this.nickname,
        todoStarCount = this.todoStarCount,
        habitStarCount = this.habitStarCount,
        weekRange = this.weekRange,
    )
