package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.GrowthDataSource
import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel
import com.swyp.firsttodo.domain.model.growth.GrowthModel
import com.swyp.firsttodo.domain.repository.GrowthRepository
import javax.inject.Inject

class GrowthRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val growthDataSource: GrowthDataSource,
    ) : GrowthRepository {
        override suspend fun getGrowthTodo(): Result<GrowthModel> =
            apiResponseHandler.safeApiCall {
                growthDataSource.getGrowthTodo()
            }.map { it.toModel() }

        override suspend fun getGrowthHabit(): Result<GrowthModel> =
            apiResponseHandler.safeApiCall {
                growthDataSource.getGrowthHabit()
            }.map { it.toModel() }

        override suspend fun getGrowthChildren(): Result<List<ChildGrowthModel>> =
            apiResponseHandler.safeApiCall {
                growthDataSource.getGrowthChildren()
            }.map { it.toModel() }
    }
