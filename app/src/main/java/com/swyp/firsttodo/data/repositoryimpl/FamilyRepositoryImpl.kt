package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.FamilyDataSource
import com.swyp.firsttodo.data.remote.dto.request.family.FamilyConnectRequestBody
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.domain.model.family.FamilyInfo
import com.swyp.firsttodo.domain.repository.FamilyRepository
import com.swyp.firsttodo.domain.throwable.FamilyError
import javax.inject.Inject

class FamilyRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val familyDataSource: FamilyDataSource,
    ) : FamilyRepository {
        override suspend fun connectFamily(inviteCode: String): Result<Unit> =
            apiResponseHandler.safeApiCall {
                familyDataSource.postFamilyConnect(FamilyConnectRequestBody(inviteCode))
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = when (throwable) {
                        is ApiError.BadRequest -> when (throwable.code) {
                            40015 -> FamilyError.InviteCodeEmpty(throwable.serverMsg)
                            40019 -> FamilyError.InviteCodeMySelf(throwable.serverMsg)
                            else -> throwable
                        }

                        is ApiError.NotFound -> FamilyError.InviteCodeInvalid(throwable.serverMsg)

                        is ApiError.Conflict -> FamilyError.InviteAlreadyDone(throwable.serverMsg)

                        else -> throwable
                    }

                    Result.failure(error)
                },
            )

        override suspend fun getConnectedFamily(): Result<List<ConnectedFamilyModel>> =
            apiResponseHandler.safeApiCall {
                familyDataSource.getConnectedFamily()
            }.map { it.toModel() }

        override suspend fun disconnectFamily(targetUserId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                familyDataSource.deleteFamilyConnect(targetUserId)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { throwable ->
                    val error = if (throwable is ApiError.BadRequest) {
                        when (throwable.code) {
                            40405 -> FamilyError.ConnectInvalid(throwable.serverMsg)
                            else -> throwable
                        }
                    } else {
                        throwable
                    }
                    Result.failure(error)
                },
            )

        override suspend fun getFamilyDashboard(): Result<List<FamilyInfo>> =
            apiResponseHandler.safeApiCall {
                familyDataSource.getFamilyDashboard()
            }.map { it.toModel() }
    }
