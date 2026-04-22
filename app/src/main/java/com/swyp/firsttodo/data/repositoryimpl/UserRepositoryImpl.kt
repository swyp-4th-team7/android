package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.UserDataSource
import com.swyp.firsttodo.data.remote.dto.request.user.ProfileRequestDto
import com.swyp.firsttodo.domain.model.user.MyInfoModel
import com.swyp.firsttodo.domain.repository.UserRepository
import com.swyp.firsttodo.domain.throwable.ProfileError
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val userDataSource: UserDataSource,
    ) : UserRepository {
        override suspend fun getMyInfo(): Result<MyInfoModel> =
            apiResponseHandler.safeApiCall { userDataSource.getMyInfo() }.map { it.toModel() }

        override suspend fun logout(): Result<Unit> {
            val remoteResult = apiResponseHandler.safeApiCall {
                userDataSource.deleteLogout()
            }

            val localResult = suspendRunCatching { sessionManager.logout() }

            return if (remoteResult.isSuccess && localResult.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(
                    remoteResult.exceptionOrNull() ?: localResult.exceptionOrNull()
                        ?: Exception("Both fail"),
                )
            }
        }

        override suspend fun deleteAccount(): Result<Unit> {
            val remoteResult = apiResponseHandler.safeApiCall {
                userDataSource.deleteAccount()
            }

            val localResult = suspendRunCatching { sessionManager.logout() }

            return if (remoteResult.isSuccess && localResult.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(
                    remoteResult.exceptionOrNull() ?: localResult.exceptionOrNull()
                        ?: Exception("Both fail"),
                )
            }
        }

        override suspend fun updateProfile(
            nickname: String,
            userType: String,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                userDataSource.patchProfile(ProfileRequestDto(nickname, userType))
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError -> when (e.code) {
                        40005 -> ProfileError.NicknameEmpty(e.serverMsg)
                        40006 -> ProfileError.NicknameLength(e.serverMsg)
                        40007 -> ProfileError.NicknameSymbols(e.serverMsg)
                        40008 -> ProfileError.RoleEmpty(e.serverMsg)
                        else -> e
                    }

                    else -> e
                }
            }

        override suspend fun updateTerms(): Result<Unit> =
            apiResponseHandler.safeApiCall { userDataSource.patchTerms() }
    }
