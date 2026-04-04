package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.domain.model.family.FamilyInfo

interface FamilyRepository {
    suspend fun connectFamily(inviteCode: String): Result<Unit>

    suspend fun getConnectedFamily(): Result<List<ConnectedFamilyModel>>

    suspend fun disconnectFamily(targetUserId: Long): Result<Unit>

    suspend fun getFamilyDashboard(): Result<List<FamilyInfo>>

    suspend fun getMyInviteCode(): Result<String>
}
