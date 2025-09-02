package com.alarmy.near.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 토큰 갱신 응답 모델
 */
@Serializable
data class TokenRefreshResponse(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshTokenInfo")
    val refreshTokenInfo: RefreshTokenInfo? = null,
)
