package com.alarmy.near.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 소셜 로그인 응답 데이터 클래스
@Serializable
data class LoginResponse(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String? = null,
)
