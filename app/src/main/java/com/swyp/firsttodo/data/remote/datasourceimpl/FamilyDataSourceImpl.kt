package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.FamilyDataSource
import com.swyp.firsttodo.data.remote.dto.request.family.FamilyConnectRequestBody
import com.swyp.firsttodo.data.remote.dto.response.family.ConnectedFamilyResponseDto
import com.swyp.firsttodo.data.remote.dto.response.family.FamilyDashboardResponseDto
import com.swyp.firsttodo.data.remote.service.FamilyService
import javax.inject.Inject

class FamilyDataSourceImpl
    @Inject
    constructor(
        private val familyService: FamilyService,
    ) : FamilyDataSource {
        override suspend fun postFamilyConnect(request: FamilyConnectRequestBody): BaseResponse<Unit> =
            familyService.postFamilyConnect(request)

        override suspend fun getConnectedFamily(): BaseResponse<ConnectedFamilyResponseDto> =
            familyService.getConnectedFamily()

        override suspend fun deleteFamilyConnect(targetUserId: Long): BaseResponse<Unit> =
            familyService.deleteFamilyConnect(targetUserId)

        override suspend fun getFamilyDashboard(): BaseResponse<FamilyDashboardResponseDto> =
            familyService.getFamilyDashboard()

        override suspend fun getMyInviteCode(): BaseResponse<String> = familyService.getMyInviteCode()
    }
