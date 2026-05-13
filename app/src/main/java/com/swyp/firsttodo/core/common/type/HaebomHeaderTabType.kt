package com.swyp.firsttodo.core.common.type

interface HaebomHeaderTabType {
    val index: Int
    val label: String
}

enum class GrowthHeaderTabType(
    override val index: Int,
    override val label: String,
) : HaebomHeaderTabType {
    TODO(0, "할 일"),
    REWARD(1, "보상"),
}
