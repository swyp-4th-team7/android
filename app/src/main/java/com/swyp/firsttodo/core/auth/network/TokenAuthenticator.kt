package com.swyp.firsttodo.core.auth.network

import com.swyp.firsttodo.core.auth.datasource.TokenDataSource
import com.swyp.firsttodo.core.auth.manager.AuthManager
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

// 401 발생 시 토큰 재발급
class TokenAuthenticator
    @Inject
    constructor(
        private val authManager: AuthManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val tokenDataSource: TokenDataSource,
        private val authDataSource: AuthDataSource,
    ) : Authenticator {
        @Synchronized
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            val requestUrl = response.request.url.toString()

            if (requestUrl.contains(REISSUE_END_POINT)) {
                Timber.w("Refresh token expired. Need login.")
                runBlocking { authManager.logout() }
                return null
            }

            // 먼저 발생한 요청이 이미 토큰 갱신 완료했다면 리이슈 하지 않음
            val requestToken = response.request.header(AUTHORIZATION)
            val storedToken = runBlocking { tokenDataSource.getAccessToken() } ?: return null

            if (requestToken != "$BEARER $storedToken") {
                return response.request.newBuilder()
                    .header(AUTHORIZATION, "$BEARER $storedToken")
                    .build()
            }

            val refreshToken = runBlocking { tokenDataSource.getRefreshToken() }

            if (refreshToken.isNullOrBlank()) {
                Timber.w("Cannot find refreshToken.")
                runBlocking { authManager.logout() }
                return null
            }

            val reissueResult = runBlocking {
                apiResponseHandler.safeApiCall {
                    authDataSource.postReissue(refreshToken)
                }
            }

            val newTokens = reissueResult.getOrElse {
                Timber.e(it, "Reissue fail.")
                runBlocking { authManager.logout() }
                return null
            }

            runBlocking {
                tokenDataSource.saveTokens(newTokens.accessToken, newTokens.refreshToken)
            }

            return response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER ${newTokens.accessToken}")
                .build()
        }

        companion object {
            private const val REISSUE_END_POINT = "/auth/reissue"
            private const val AUTHORIZATION = "Authorization"
            private const val BEARER = "Bearer"
        }
    }
