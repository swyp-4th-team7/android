package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel
import com.swyp.firsttodo.domain.model.growth.GrowthModel

interface GrowthRepository {
    suspend fun getGrowthTodo(): Result<GrowthModel>

    suspend fun getGrowthHabit(): Result<GrowthModel>

    suspend fun getGrowthChildren(): Result<List<ChildGrowthModel>>
}
