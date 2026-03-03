package com.swyp.firsttodo.core.auth.network

import com.swyp.firsttodo.core.auth.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// 헤더에 accessToken 삽입
class AuthInterceptor
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val accessToken = runBlocking { tokenDataSource.getAccessToken() }

            val newRequest = chain.request().newBuilder().apply {
                if (!accessToken.isNullOrBlank()) {
                    addHeader(AUTHORIZATION, "$BEARER $accessToken")
                }
            }.build()

            return chain.proceed(newRequest)
        }

        companion object {
            private const val AUTHORIZATION = "Authorization"
            private const val BEARER = "Bearer"
        }
    }
