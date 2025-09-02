package com.alarmy.near.network.service

import com.alarmy.near.network.request.SocialLoginRequest
import com.alarmy.near.network.request.TokenRefreshRequest
import com.alarmy.near.network.response.LoginResponse
import com.alarmy.near.network.response.TokenRefreshResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 인증 관련 API 서비스
 */
interface AuthService {
    // 소셜 로그인 API 호출
    @POST("/auth/social")
    suspend fun socialLogin(
        @Body request: SocialLoginRequest,
    ): LoginResponse

    // 토큰 갱신 API 호출
    @POST("/auth/renew")
    suspend fun renewToken(
        @Body request: TokenRefreshRequest,
    ): TokenRefreshResponse
}
