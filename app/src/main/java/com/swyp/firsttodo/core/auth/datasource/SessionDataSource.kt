package com.swyp.firsttodo.core.auth.datasource

interface SessionDataSource {
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun getUserType(): String?

    suspend fun getProfileCompleted(): Boolean?

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun saveSession(
        userType: String,
        profileCompleted: Boolean,
    )

    suspend fun clearTokens()
}
