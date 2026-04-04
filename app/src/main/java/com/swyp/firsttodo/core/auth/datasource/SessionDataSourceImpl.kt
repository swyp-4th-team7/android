package com.swyp.firsttodo.core.auth.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SessionDataSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : SessionDataSource {
        private val tokenPrefs = dataStore.data.distinctUntilChanged()

        @Volatile
        private var cachedAccessToken: String? = null

        @Volatile
        private var cachedRefreshToken: String? = null

        @Volatile
        private var cachedUserType: String? = null

        @Volatile
        private var cachedProfileCompleted: Boolean? = null

        private suspend fun ensureInitialized() {
            val prefs = tokenPrefs.firstOrNull()

            cachedAccessToken = prefs?.get(ACCESS_TOKEN_KEY)
            cachedRefreshToken = prefs?.get(REFRESH_TOKEN_KEY)
            cachedUserType = prefs?.get(USER_TYPE_KEY)
            cachedProfileCompleted = prefs?.get(PROFILE_COMPLETED_KEY)
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

        override suspend fun getUserType(): String? {
            if (cachedUserType == null) {
                ensureInitialized()
            }
            return cachedUserType
        }

        override suspend fun getProfileCompleted(): Boolean? {
            if (cachedProfileCompleted == null) {
                ensureInitialized()
            }
            return cachedProfileCompleted
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

        override suspend fun saveSession(
            userType: String,
            profileCompleted: Boolean,
        ) {
            dataStore.edit { prefs ->
                prefs[USER_TYPE_KEY] = userType
                prefs[PROFILE_COMPLETED_KEY] = profileCompleted
            }
            cachedUserType = userType
            cachedProfileCompleted = profileCompleted
        }

        override suspend fun clearTokens() {
            dataStore.edit { prefs ->
                prefs.remove(ACCESS_TOKEN_KEY)
                prefs.remove(REFRESH_TOKEN_KEY)
                prefs.remove(USER_TYPE_KEY)
                prefs.remove(PROFILE_COMPLETED_KEY)
            }
            cachedAccessToken = null
            cachedRefreshToken = null
            cachedUserType = null
            cachedProfileCompleted = null
        }

        companion object {
            private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN_KEY")
            private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
            private val USER_TYPE_KEY = stringPreferencesKey("USER_TYPE_KEY")
            private val PROFILE_COMPLETED_KEY = booleanPreferencesKey("PROFILE_COMPLETED_KEY")
        }
    }
