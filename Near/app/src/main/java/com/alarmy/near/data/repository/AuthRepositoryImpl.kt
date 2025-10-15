package com.alarmy.near.data.repository

import com.alarmy.near.core.exception.ExceptionMapper
import com.alarmy.near.data.datasource.SocialLoginProcessor
import com.alarmy.near.model.ProviderType
import com.alarmy.near.network.auth.TokenManager
import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.service.AuthService
import com.alarmy.near.utils.extensions.apiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val socialLoginProcessor: SocialLoginProcessor,
        private val tokenManager: TokenManager,
    ) : AuthRepository {
        override suspend fun performSocialLogin(providerType: ProviderType): Result<Unit> =
            try {
                val result = socialLoginProcessor.processLogin(providerType)

                if (result.isSuccess) {
                    val accessToken = result.getOrThrow()
                    apiCallFlow { socialLoginInternal(accessToken, providerType) }.first()
                    Result.success(Unit)
                } else {
                    Result.failure(
                        ExceptionMapper.mapToAppException(
                            result.exceptionOrNull() ?: Exception("로그인에 실패했습니다"),
                        ),
                    )
                }
            } catch (exception: Exception) {
                Result.failure(ExceptionMapper.mapToAppException(exception))
            }

        override suspend fun socialLogin(
            accessToken: String,
            providerType: ProviderType,
        ): Result<Unit> =
            try {
                apiCallFlow { socialLoginInternal(accessToken, providerType) }.first()
                Result.success(Unit)
            } catch (exception: Exception) {
                Result.failure(ExceptionMapper.mapToAppException(exception))
            }

        /**
         * 소셜 로그인 내부 구현 (safeApiCallFlow에서 사용)
         */
        private suspend fun socialLoginInternal(
            accessToken: String,
            providerType: ProviderType,
        ) {
            val request =
                SocialLoginRequest(
                    accessToken = accessToken,
                    providerType = providerType.name,
                )

            val response = authService.socialLogin(request)

            // 토큰 저장
            val expiresIn = tokenManager.calculateExpiresIn(response.refreshTokenInfo?.expiresAt)
            tokenManager.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshTokenInfo?.token,
                expiresIn = expiresIn,
            )
        }

        override suspend fun logout() {
            tokenManager.clearAllTokens()
        }

        override suspend fun isLoggedIn(): Boolean =
            try {
                tokenManager.hasValidToken()
            } catch (exception: Exception) {
                false
            }

        override suspend fun getCurrentUserToken(): String? =
            try {
                tokenManager.getAccessToken()
            } catch (exception: Exception) {
                null
            }

        override fun observeLoginStatus(): Flow<Boolean> = tokenManager.observeLoginStatus()

        override suspend fun refreshToken(): Boolean = tokenManager.refreshToken()
    }
