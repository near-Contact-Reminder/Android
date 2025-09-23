package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

/**
 * 회원 탈퇴 API 요청 데이터 모델
 * Network Layer에서 사용하는 요청 데이터
 */
@Serializable
data class WithdrawRequest(
    val reasonType: String,
    val customReason: String? = null
)
