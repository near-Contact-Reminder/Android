package com.alarmy.near.model.member

import kotlinx.serialization.Serializable

/**
 * 회원 정보 응답 모델
 */
@Serializable
data class MemberInfo(
    val memberId: String,
    val username: String,
    val nickname: String,
    val imageUrl: String?,
    val notificationAgreedAt: String?,
    val providerType: String,
)
