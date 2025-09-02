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

@Serializable
data class RefreshTokenInfo(
    @SerialName("token")
    val token: String,
    @SerialName("expiresAt")
    val expiresAt: String, // "2025-08-20 03:02:07" 형식
)
