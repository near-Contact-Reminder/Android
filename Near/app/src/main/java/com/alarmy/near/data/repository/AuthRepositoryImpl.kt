package com.alarmy.near.data.repository

import com.alarmy.near.data.datasource.SocialLoginProcessor
import com.alarmy.near.data.local.datastore.TokenPreferences
import com.alarmy.near.model.LoginResult
import com.alarmy.near.model.ProviderType
import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.request.TokenRefreshRequest
import com.alarmy.near.network.service.AuthService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val socialLoginProcessor: SocialLoginProcessor,
        private val tokenPreferences: TokenPreferences,
    ) : AuthRepository {
        override suspend fun performSocialLogin(providerType: ProviderType): LoginResult =
            try {
                val result = socialLoginProcessor.processLogin(providerType)

                if (result.isSuccess) {
                    val accessToken = result.getOrThrow()
                    socialLogin(accessToken, providerType)
                } else {
                    val providerName = providerType.name.lowercase()
                    LoginResult(
                        isSuccess = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "$providerName 로그인에 실패했습니다",
                    )
                }
            } catch (exception: Exception) {
                val providerName = providerType.name.lowercase()
                LoginResult(
                    isSuccess = false,
                    errorMessage = exception.message ?: "$providerName 로그인 중 오류가 발생했습니다",
                )
            }

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
                tokenPreferences.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )

                LoginResult(
                    isSuccess = true,
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
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
                tokenPreferences.clearAllTokens()
            } catch (exception: Exception) {
                throw exception
            }
        }

        override suspend fun isLoggedIn(): Boolean =
            try {
                tokenPreferences.hasValidTokens()
            } catch (exception: Exception) {
                false
            }

        override suspend fun getCurrentUserToken(): String? =
            try {
                tokenPreferences.getAccessToken()
            } catch (exception: Exception) {
                null
            }

        override fun observeLoginStatus(): Flow<Boolean> = tokenPreferences.observeLoginStatus()

        override suspend fun refreshToken(): Boolean =
            try {
                val refreshToken = tokenPreferences.getRefreshToken() ?: return false

                // 토큰 갱신 API 호출
                val request = TokenRefreshRequest(refreshToken = refreshToken)
                val response = authService.renewToken(request)

                // 새로운 토큰 저장
                tokenPreferences.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                )

                true
            } catch (exception: Exception) {
                // 토큰 갱신 실패 시 처리
                when (exception) {
                    is HttpException -> {
                        if (exception.code() == 401 || exception.code() == 403) {
                            // 리프레시 토큰도 만료된 경우 모든 토큰 삭제
                            tokenPreferences.clearAllTokens()
                        }
                    }
                }
                false
            }
    }
