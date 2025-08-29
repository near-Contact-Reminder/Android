package com.alarmy.near.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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



        /**
         * 액세스 토큰 저장
         */
        suspend fun saveAccessToken(token: String) {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = token
            }
        }

        /**
         * 리프레시 토큰 저장
         */
        suspend fun saveRefreshToken(token: String) {
            dataStore.edit { preferences ->
                preferences[refreshTokenKey] = token
            }
        }

        /**
         * 두 토큰 동시 저장
         */
        suspend fun saveTokens(
            accessToken: String,
            refreshToken: String?,
        ) {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = accessToken
                refreshToken?.let {
                    preferences[refreshTokenKey] = it
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
            return !accessToken.isNullOrBlank()
        }

        /**
         * 모든 토큰 삭제
         */
        suspend fun clearAllTokens() {
            dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(refreshTokenKey)
            }
        }



        /**
         * 로그인 상태 관찰
         */
        fun observeLoginStatus() =
            dataStore.data.map { preferences ->
                !preferences[accessTokenKey].isNullOrBlank()
            }
    }
