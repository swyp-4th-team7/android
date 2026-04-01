package com.swyp.firsttodo.domain.model.growth

data class ChildGrowthModel(
    val childId: Long,
    val nickname: String,
    val todoStarCount: Int,
    val habitStarCount: Int,
    val weekRange: String,
)
