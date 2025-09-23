package com.alarmy.near.data.entity

import kotlinx.serialization.Serializable

/**
 * 회원 탈퇴 요청 Data Layer 엔티티
 * API 요청 데이터를 나타내는 모델
 */
@Serializable
data class WithdrawRequestEntity(
    val reasonType: String,
    val customReason: String? = null
)
