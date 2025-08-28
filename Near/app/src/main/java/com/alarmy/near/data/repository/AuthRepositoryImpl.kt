package com.alarmy.near.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alarmy.near.model.LoginResult
import com.alarmy.near.model.ProviderType
import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

// DataStore 확장 프로퍼티
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

/**
 * DataStore 제공 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore
}

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val dataStore: DataStore<Preferences>,
    ) : AuthRepository {
        private val accessTokenKey = stringPreferencesKey("access_token")
        private val refreshTokenKey = stringPreferencesKey("refresh_token")

        override suspend fun socialLogin(
            accessToken: String,
            providerType: ProviderType,
        ): LoginResult =
            try {
                val request =
                    SocialLoginRequest(
                        accessToken = accessToken,
                        providerType = providerType.name,
                    )

                val response = authService.socialLogin(request)

                // 토큰 저장
                saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshTokenInfo?.token,
                )

                LoginResult(
                    isSuccess = true,
                    accessToken = response.accessToken,
                    refreshToken = response.refreshTokenInfo?.token,
                )
            } catch (exception: HttpException) {
                val errorMessage =
                    when (exception.code()) {
                        400 -> "잘못된 요청입니다"
                        401 -> "인증에 실패했습니다"
                        403 -> "접근이 거부되었습니다"
                        500 -> "서버에 문제가 발생했습니다"
                        else -> "로그인 중 오류가 발생했습니다"
                    }

                LoginResult(
                    isSuccess = false,
                    errorMessage = errorMessage,
                )
            } catch (exception: Exception) {
                val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다"
                LoginResult(
                    isSuccess = false,
                    errorMessage = errorMessage,
                )
            }

        override suspend fun logout() {
            try {
                clearTokens()
            } catch (exception: Exception) {
                throw exception
            }
        }

        override suspend fun isLoggedIn(): Boolean =
            try {
                val token = getCurrentUserToken()
                val isLoggedIn = !token.isNullOrBlank()
                isLoggedIn
            } catch (exception: Exception) {
                false
            }

        override suspend fun getCurrentUserToken(): String? =
            try {
                val token = dataStore.data.first()[accessTokenKey]
                token
            } catch (exception: Exception) {
                null
            }

        override fun observeLoginStatus(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                !preferences[accessTokenKey].isNullOrBlank()
            }

        private suspend fun saveTokens(
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

        private suspend fun clearTokens() {
            dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(refreshTokenKey)
            }
        }
    }
