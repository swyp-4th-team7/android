package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.FamilyDataSource
import com.swyp.firsttodo.data.remote.dto.request.family.FamilyConnectRequestBody
import com.swyp.firsttodo.domain.error.FamilyError
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.domain.model.family.FamilyInfo
import com.swyp.firsttodo.domain.repository.FamilyRepository
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
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError -> when (e.code) {
                        40015 -> FamilyError.InviteCodeEmpty(e.serverMsg)
                        40019 -> FamilyError.InviteCodeMySelf(e.serverMsg)
                        40404 -> FamilyError.InviteCodeInvalid(e.serverMsg)
                        40903 -> FamilyError.InviteAlreadyDone(e.serverMsg)
                        else -> e
                    }

                    else -> e
                }
            }

        override suspend fun getConnectedFamily(): Result<List<ConnectedFamilyModel>> =
            apiResponseHandler.safeApiCall {
                familyDataSource.getConnectedFamily()
            }.map { it.toModel() }

        override suspend fun disconnectFamily(targetUserId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                familyDataSource.deleteFamilyConnect(targetUserId)
            }.recoverCatching { e ->
                throw when {
                    e is ApiError.NotFound -> FamilyError.ConnectInvalid(e.serverMsg)
                    else -> e
                }
            }

        override suspend fun getFamilyDashboard(): Result<List<FamilyInfo>> =
            apiResponseHandler.safeApiCall {
                familyDataSource.getFamilyDashboard()
            }.map { it.toModel() }

        override suspend fun getMyInviteCode(): Result<String> =
            apiResponseHandler.safeApiCall {
                familyDataSource.getMyInviteCode()
            }.map { it.inviteCode }
                .recoverCatching { e ->
                    throw when {
                        e is ApiError && e.code == 40026 -> FamilyError.OnboardingUncompleted(e.serverMsg)
                        else -> e
                    }
                }
    }
