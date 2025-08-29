package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

/**
 * 토큰 갱신 요청 모델
 */
@Serializable
data class TokenRefreshRequest(
    val refreshToken: String
)
