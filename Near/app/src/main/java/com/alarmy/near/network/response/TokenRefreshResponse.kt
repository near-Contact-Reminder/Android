package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

/**
 * 토큰 갱신 응답 모델
 */
@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String? = null
)
