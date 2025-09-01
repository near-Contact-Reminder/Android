package com.alarmy.near.data.repository

import com.alarmy.near.data.datasource.SocialLoginProcessor
import com.alarmy.near.data.local.datastore.TokenPreferences
import com.alarmy.near.model.LoginResult
import com.alarmy.near.model.ProviderType
import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.request.TokenRefreshRequest
import com.alarmy.near.network.service.AuthService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale
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
            val request = SocialLoginRequest(
                accessToken = accessToken,
                providerType = providerType.name,
            )

            val response = authService.socialLogin(request)

            // 토큰 저장
            val expiresIn = calculateExpiresIn(response.refreshTokenInfo?.expiresAt)

            tokenPreferences.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshTokenInfo?.token,
                expiresIn = expiresIn,
            )

            LoginResult(
                isSuccess = true,
                accessToken = response.accessToken,
                refreshToken = response.refreshTokenInfo?.token,
            )
        } catch (exception: HttpException) {
            val errorMessage = when (exception.code()) {
                400 -> "잘못된 요청입니다"
                401 -> "소셜 로그인에 실패했습니다. 다시 시도해주세요."
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

    override suspend fun refreshToken(): Boolean = refreshTokenWithRetry()

    /**
     * 토큰 갱신 (재시도 로직 포함)
     */
    private suspend fun refreshTokenWithRetry(maxRetries: Int = 3): Boolean {
        repeat(maxRetries) { attempt ->
            try {
                val refreshToken = tokenPreferences.getRefreshToken() ?: return false

                // 토큰 갱신 API 호출
                val request = TokenRefreshRequest(refreshToken = refreshToken)
                val response = authService.renewToken(request)

                // 새로운 토큰 저장
                val expiresIn = calculateExpiresIn(response.refreshTokenInfo?.expiresAt)

                tokenPreferences.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshTokenInfo?.token,
                    expiresIn = expiresIn,
                )

                return true
            } catch (exception: Exception) {
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401, 403 -> {
                                // 리프레시 토큰도 만료된 경우 모든 토큰 삭제
                                tokenPreferences.clearAllTokens()
                                return false // 재시도 불가
                            }

                            else -> return false
                        }
                    }

                    else -> {
                        if (attempt < maxRetries - 1) {
                            delay(1000L * (attempt + 1))
                            return@repeat
                        }
                    }
                }
            }
        }
        return false
    }

    /**
     * 만료 시간 계산 (초 단위)
     */
    private fun calculateExpiresIn(expiresAtString: String?): Long? {
        return expiresAtString?.let { expiresAt ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val expiresAtTime = dateFormat.parse(expiresAt)
            val currentTime = System.currentTimeMillis()
            ((expiresAtTime?.time ?: currentTime) - currentTime) / 1000 // 초 단위로 변환
        }
    }
}
