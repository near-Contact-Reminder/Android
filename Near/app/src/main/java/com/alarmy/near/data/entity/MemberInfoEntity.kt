package com.alarmy.near.data.entity

import kotlinx.serialization.Serializable

/**
 * 회원 정보 Data Layer 엔티티
 * API 응답 데이터를 나타내는 모델
 */
@Serializable
data class MemberInfoEntity(
    val memberId: String,
    val username: String,
    val nickname: String,
    val imageUrl: String?,
    val notificationAgreedAt: String? = "", // TODO #Issue: 52 Default 값 추후 제거 필요
    val providerType: String,
)
