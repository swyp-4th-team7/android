package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.family.FamilyConnectRequestBody
import com.swyp.firsttodo.data.remote.dto.response.family.ConnectedFamilyResponseDto
import com.swyp.firsttodo.data.remote.dto.response.family.FamilyDashboardResponseDto

interface FamilyDataSource {
    suspend fun postFamilyConnect(request: FamilyConnectRequestBody): BaseResponse<Unit>

    suspend fun getConnectedFamily(): BaseResponse<ConnectedFamilyResponseDto>

    suspend fun deleteFamilyConnect(targetUserId: Long): BaseResponse<Unit>

    suspend fun getFamilyDashboard(): BaseResponse<FamilyDashboardResponseDto>

    suspend fun getMyInviteCode(): BaseResponse<String>
}
