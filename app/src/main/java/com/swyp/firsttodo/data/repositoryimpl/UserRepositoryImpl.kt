package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.auth.manager.AuthManager
import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.remote.datasource.UserDataSource
import com.swyp.firsttodo.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val authManager: AuthManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val userDataSource: UserDataSource,
    ) : UserRepository {
        override suspend fun logout(): Result<Unit> {
            val remoteResult = apiResponseHandler.safeApiCall {
                userDataSource.deleteLogout()
            }

            val localResult = suspendRunCatching { authManager.logout() }

            return if (remoteResult.isSuccess && localResult.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(
                    remoteResult.exceptionOrNull() ?: localResult.exceptionOrNull() ?: Exception("Both fail"),
                )
            }
        }
    }
