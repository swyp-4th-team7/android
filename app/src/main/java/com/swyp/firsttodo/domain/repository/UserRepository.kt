package com.swyp.firsttodo.domain.repository

interface UserRepository {
    suspend fun logout(): Result<Unit>
}
