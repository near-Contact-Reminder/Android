package com.alarmy.near.model.member

import kotlinx.serialization.Serializable

/**
 * 회원 탈퇴 요청 데이터 모델
 */
@Serializable
data class WithdrawRequest(
    val reasonType: String,
    val customReason: String? = null
)
