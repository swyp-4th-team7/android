package com.swyp.firsttodo.domain.repository

interface UserRepository {
    suspend fun logout(): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>

    suspend fun updateProfile(): Result<Unit>

    suspend fun updateTerms(): Result<Unit>
}
