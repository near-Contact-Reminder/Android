package com.alarmy.near.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 소셜 로그인 요청 데이터 클래스
 * 
 * @param accessToken 소셜 로그인 AccessToken
 * @param providerType 소셜 로그인 Provider 타입 (KAKAO, APPLE 등)
 */
@Serializable
data class SocialLoginRequest(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("providerType")
    val providerType: String,
)
