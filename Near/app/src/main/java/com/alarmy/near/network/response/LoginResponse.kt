package com.alarmy.near.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 소셜 로그인 응답 데이터 클래스
@Serializable
data class LoginResponse(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshTokenInfo")
    val refreshTokenInfo: RefreshTokenInfo? = null,
)

// 리프레시 토큰 정보
@Serializable
data class RefreshTokenInfo(
    @SerialName("token")
    val token: String,
    @SerialName("expiresAt")
    val expiresAt: String,
)
