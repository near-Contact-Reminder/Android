package com.alarmy.near.data.entity

import kotlinx.serialization.Serializable

/**
 * 회원 탈퇴 요청 Data Layer 엔티티
 * Network Layer의 WithdrawRequest와 동일한 구조
 */
@Serializable
data class WithdrawRequestEntity(
    val reasonType: String,
    val customReason: String? = null,
)
