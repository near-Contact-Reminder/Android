package com.alarmy.near.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 저장소 DataStore 관리 클래스
 * 액세스 토큰과 리프레시 토큰의 저장, 조회, 삭제를 담당
 */
@Singleton
class TokenPreferences
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        private val accessTokenKey = stringPreferencesKey("access_token")
        private val refreshTokenKey = stringPreferencesKey("refresh_token")
        private val expiresAtKey = longPreferencesKey("expires_at")

        /**
         * 두 토큰 동시 저장
         */
        suspend fun saveTokens(
            accessToken: String,
            refreshToken: String?,
            expiresIn: Long? = null,
        ) {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = accessToken
                refreshToken?.let {
                    preferences[refreshTokenKey] = it
                }
                expiresIn?.let {
                    val expiresAt = System.currentTimeMillis() + (it * 1000)
                    preferences[expiresAtKey] = expiresAt
                }
            }
        }

        /**
         * 액세스 토큰 조회
         */
        suspend fun getAccessToken(): String? = dataStore.data.first()[accessTokenKey]

        /**
         * 리프레시 토큰 조회
         */
        suspend fun getRefreshToken(): String? = dataStore.data.first()[refreshTokenKey]

        /**
         * 토큰 존재 여부 확인
         */
        suspend fun hasValidTokens(): Boolean {
            val accessToken = getAccessToken()
            return !accessToken.isNullOrBlank() && !isTokenExpired()
        }

        /**
         * 토큰 만료 여부 확인
         */
        suspend fun isTokenExpired(): Boolean {
            val expiresAt = dataStore.data.first()[expiresAtKey] ?: return true
            return System.currentTimeMillis() >= expiresAt
        }

        /**
         * 토큰 만료 시간 조회
         */
        suspend fun getTokenExpiresAt(): Long? = dataStore.data.first()[expiresAtKey]

        /**
         * 모든 토큰 삭제
         */
        suspend fun clearAllTokens() {
            dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(refreshTokenKey)
                preferences.remove(expiresAtKey)
            }
        }

        /**
         * 액세스 토큰 관찰
         */
        fun observeAccessToken() =
            dataStore.data.map { preferences ->
                preferences[accessTokenKey]
            }

        /**
         * 로그인 상태 관찰
         */
        fun observeLoginStatus() =
            dataStore.data.map { preferences ->
                !preferences[accessTokenKey].isNullOrBlank()
            }
    }
