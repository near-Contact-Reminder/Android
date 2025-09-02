package com.alarmy.near.data.repository

import com.alarmy.near.data.datasource.SocialLoginProcessor
import com.alarmy.near.model.ProviderType
import com.alarmy.near.network.auth.TokenManager
import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.service.AuthService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
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
                    socialLogin(accessToken, providerType)
                } else {
                    Result.failure(
                        createLoginException(
                            providerType = providerType,
                            errorMessage = result.exceptionOrNull()?.message,
                            defaultMessage = "로그인에 실패했습니다",
                        ),
                    )
                }
            } catch (exception: Exception) {
                Result.failure(
                    createLoginException(
                        providerType = providerType,
                        errorMessage = exception.message,
                        defaultMessage = "로그인 중 오류가 발생했습니다",
                    ),
                )
            }

        override suspend fun socialLogin(
            accessToken: String,
            providerType: ProviderType,
        ): Result<Unit> =
            try {
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

                Result.success(Unit)
            } catch (exception: HttpException) {
                val errorMessage = getHttpErrorMessage(exception.code())
                Result.failure(Exception(errorMessage))
            } catch (exception: Exception) {
                val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다"
                Result.failure(Exception(errorMessage))
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

        private fun createLoginException(
            providerType: ProviderType,
            errorMessage: String?,
            defaultMessage: String,
        ): Exception {
            val finalMessage = errorMessage ?: "$providerType $defaultMessage"
            return Exception(finalMessage)
        }

        // TODO 추후 에러 메시지 변경
        private fun getHttpErrorMessage(httpCode: Int): String =
            when (httpCode) {
                400 -> "잘못된 요청입니다"
                401 -> "소셜 로그인에 실패했습니다. 다시 시도해주세요."
                403 -> "접근이 거부되었습니다"
                500 -> "서버에 문제가 발생했습니다"
                else -> "로그인 중 오류가 발생했습니다"
            }
    }
