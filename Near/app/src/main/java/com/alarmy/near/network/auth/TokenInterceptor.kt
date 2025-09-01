package com.alarmy.near.network.auth

import com.alarmy.near.data.local.datastore.TokenPreferences
import com.alarmy.near.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 인터셉터
 * Authorization 헤더를 자동으로 추가하고 인증이 필요없는 요청은 제외
 */
@Singleton
class TokenInterceptor
@Inject
constructor(
    private val tokenPreferences: TokenPreferences,
    private val authRepository: AuthRepository,
) : Interceptor {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentToken: String? = null
    private var tokenExpiresAt: Long? = null
    private var isRefreshing = false

    init {
        observeTokenChanges()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (isAuthExcludedRequest(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        // 현재 토큰 사용
        val validToken = getValidToken()
        val requestWithAuth = if (validToken != null) {
            originalRequest
                .newBuilder()
                .header("Authorization", "Bearer $validToken")
                .build()
        } else {
            originalRequest
        }

        // 첫 번째 요청 시도
        val response = chain.proceed(requestWithAuth)

        // 401 에러인 경우 자동 토큰 갱신 시도
        if (response.code == 401 && validToken != null) {
            val refreshed = attemptTokenRefresh()
            if (refreshed) {
                // 토큰 갱신 성공 시 원래 요청 재시도
                val newToken = getValidToken()
                if (newToken != null) {
                    val retryRequest = originalRequest
                        .newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    return chain.proceed(retryRequest)
                }
            } else {
                // 토큰 갱신 실패 시 토큰 삭제
                handleTokenExpired()
            }
        }

        return response
    }

    /**
     * 토큰 변화 관찰
     */
    private fun observeTokenChanges() {
        coroutineScope.launch {
            try {
                // 초기 토큰 로드
                currentToken = tokenPreferences.getAccessToken()
                tokenExpiresAt = tokenPreferences.getTokenExpiresAt()

                // 토큰 변화 실시간 관찰
                tokenPreferences.observeAccessToken().collect { token ->
                    currentToken = token
                    // 토큰이 변경되면 만료 시간도 다시 로드
                    if (token != null) {
                        tokenExpiresAt = tokenPreferences.getTokenExpiresAt()
                    } else {
                        tokenExpiresAt = null
                    }
                }
            } catch (e: Exception) {
                currentToken = null
                tokenExpiresAt = null
            }
        }
    }

    /**
     * 유효한 토큰 반환 (만료 검사 포함)
     */
    private fun getValidToken(): String? {
        val token = currentToken
        val expiresAt = tokenExpiresAt

        return if (token != null && !isTokenExpired(expiresAt)) {
            token
        } else {
            null
        }
    }

    /**
     * 토큰 만료 검사
     */
    private fun isTokenExpired(expiresAt: Long?): Boolean {
        if (expiresAt == null) return true
        return System.currentTimeMillis() >= expiresAt
    }

    /**
     * 자동 토큰 갱신 시도 (빅테크 수준)
     */
    private fun attemptTokenRefresh(): Boolean {
        if (isRefreshing) {
            return false // 이미 갱신 중이면 대기
        }

        return runBlocking {
            try {
                isRefreshing = true
                authRepository.refreshToken()
            } catch (e: Exception) {
                false
            } finally {
                isRefreshing = false
            }
        }
    }

    /**
     * 토큰 만료 처리
     */
    private fun handleTokenExpired() {
        currentToken = null
        tokenExpiresAt = null
        coroutineScope.launch {
            tokenPreferences.clearAllTokens()
        }
    }

    /**
     * 인증이 필요없는 요청인지 확인
     */
    private fun isAuthExcludedRequest(request: Request): Boolean {
        val url = request.url.toString()
        return AuthEndpoint.EXCLUDED_PATHS.any { url.contains(it) }
    }

    companion object {
        enum class AuthEndpoint(val path: String) {
            SOCIAL_LOGIN("/auth/social"),
            TOKEN_RENEW("/auth/renew");

            companion object {
                val EXCLUDED_PATHS = listOf(
                    SOCIAL_LOGIN.path,
                    TOKEN_RENEW.path,
                )
            }
        }
    }
}
