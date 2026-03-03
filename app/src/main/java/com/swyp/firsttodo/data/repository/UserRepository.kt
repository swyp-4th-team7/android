package com.swyp.firsttodo.data.repository

interface UserRepository {
    suspend fun logout(): Result<Unit>
}
