package com.alarmy.near.network.auth

import com.alarmy.near.network.model.AuthEndpoint
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
    private val tokenManager: TokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (isAuthExcludedRequest(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        // 현재 토큰을 헤더에 추가
        val token = runBlocking { tokenManager.getAccessToken() }
        val requestWithAuth = if (token != null) {
            originalRequest
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        // 요청 실행 (401 에러는 Authenticator에서 처리)
        return chain.proceed(requestWithAuth)
    }

    /**
     * 인증이 필요없는 요청인지 확인
     */
    private fun isAuthExcludedRequest(request: Request): Boolean {
        val url = request.url.toString()
        return AuthEndpoint.excludedPaths.any { url.contains(it) }
    }
}
