package com.swyp.firsttodo.core.auth.network

import com.swyp.firsttodo.core.auth.datasource.SessionDataSource
import com.swyp.firsttodo.core.auth.manager.SessionManager
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
        private val sessionManager: SessionManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val sessionDataSource: SessionDataSource,
        private val authDataSource: AuthDataSource,
    ) : Authenticator {
        @Synchronized
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (responseCount(response) >= 2) {
                Timber.w("Auth retry limit reached. Stop reissuing.")
                return abortWithLogout()
            }

            val requestUrl = response.request.url.toString()

            if (requestUrl.contains(REISSUE_END_POINT)) {
                Timber.w("Refresh token expired. Need login.")
                return abortWithLogout()
            }

            // 먼저 발생한 요청이 이미 토큰 갱신 완료했다면 리이슈 하지 않음
            val requestToken = response.request.header(AUTHORIZATION)
            val storedToken = runBlocking { sessionDataSource.getAccessToken() } ?: abortWithLogout()

            if (requestToken != "$BEARER $storedToken") {
                return response.request.newBuilder()
                    .header(AUTHORIZATION, "$BEARER $storedToken")
                    .build()
            }

            val refreshToken = runBlocking { sessionDataSource.getRefreshToken() }

            if (refreshToken.isNullOrBlank()) {
                Timber.w("Cannot find refreshToken.")
                return abortWithLogout()
            }

            val reissueResult = runBlocking {
                apiResponseHandler.safeApiCall {
                    authDataSource.postReissue(refreshToken)
                }
            }

            val newTokens = reissueResult.getOrElse {
                Timber.e(it, "Reissue fail.")
                return abortWithLogout()
            }

            runBlocking {
                sessionDataSource.saveTokens(newTokens.accessToken, newTokens.refreshToken)
            }

            return response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER ${newTokens.accessToken}")
                .build()
        }

        private fun abortWithLogout(): Nothing? {
            runBlocking { sessionManager.clearSession() }
            return null
        }

        private fun responseCount(response: Response): Int {
            var count = 1
            var prior = response.priorResponse
            while (prior != null) {
                count++
                prior = prior.priorResponse
            }
            return count
        }

        companion object {
            private const val REISSUE_END_POINT = "/auth/reissue"
            private const val AUTHORIZATION = "Authorization"
            private const val BEARER = "Bearer"
        }
    }
