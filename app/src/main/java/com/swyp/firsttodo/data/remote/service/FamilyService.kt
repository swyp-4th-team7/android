package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.family.FamilyConnectRequestBody
import com.swyp.firsttodo.data.remote.dto.response.family.ConnectedFamilyResponseDto
import com.swyp.firsttodo.data.remote.dto.response.family.FamilyDashboardResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FamilyService {
    @POST("/api/v1/family/connect")
    suspend fun postFamilyConnect(
        @Body request: FamilyConnectRequestBody,
    ): BaseResponse<Unit>

    @GET("/api/v1/family")
    suspend fun getConnectedFamily(): BaseResponse<ConnectedFamilyResponseDto>

    @DELETE("/api/v1/family/{targetUserId}")
    suspend fun deleteFamilyConnect(
        @Path("targetUserId") targetUserId: Long,
    ): BaseResponse<Unit>

    @GET("/api/v1/family/dashboard")
    suspend fun getFamilyDashboard(): BaseResponse<FamilyDashboardResponseDto>

    @GET("/api/v1/users/me/invite-code")
    suspend fun getMyInviteCode(): BaseResponse<String>
}
