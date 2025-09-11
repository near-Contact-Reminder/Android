package com.alarmy.near.presentation.feature.myprofile.model

/**
 * 탈퇴 사유 데이터 클래스
 */
data class WithdrawReasonItem(
    val reason: WithdrawReason,
    val isSelected: Boolean = false
)
