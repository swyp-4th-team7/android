package com.swyp.firsttodo.core.auth.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenDataSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : TokenDataSource {
        private val tokenPrefs = dataStore.data.distinctUntilChanged()

        @Volatile
        private var cachedAccessToken: String? = null

        @Volatile
        private var cachedRefreshToken: String? = null

        private suspend fun ensureInitialized() {
            val prefs = tokenPrefs.firstOrNull()

            cachedAccessToken = prefs?.get(ACCESS_TOKEN_KEY)
            cachedRefreshToken = prefs?.get(REFRESH_TOKEN_KEY)
        }

        override suspend fun getAccessToken(): String? {
            if (cachedAccessToken == null) {
                ensureInitialized()
            }
            return cachedAccessToken
        }

        override suspend fun getRefreshToken(): String? {
            if (cachedRefreshToken == null) {
                ensureInitialized()
            }
            return cachedRefreshToken
        }

        override suspend fun saveTokens(
            accessToken: String,
            refreshToken: String,
        ) {
            dataStore.edit { prefs ->
                prefs[ACCESS_TOKEN_KEY] = accessToken
                prefs[REFRESH_TOKEN_KEY] = refreshToken
            }
            cachedAccessToken = accessToken
            cachedRefreshToken = refreshToken
        }

        override suspend fun clearTokens() {
            dataStore.edit { prefs ->
                prefs.remove(ACCESS_TOKEN_KEY)
                prefs.remove(REFRESH_TOKEN_KEY)
            }
            cachedAccessToken = null
            cachedRefreshToken = null
        }

        companion object {
            private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN_KEY")
            private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
        }
    }
