package com.alarmy.near.presentation.feature.myprofile.model

/**
 * 탈퇴 사유를 나타내는 enum
 */
enum class WithdrawReason(val displayText: String) {
    REASON_DONT_USE_OFTEN("자주 이용하지 않아요"),
    REASON_NEW_ACCOUNT("신규 계정으로 가입할게요"),
    REASON_WORRIED_INFORMATION("개인정보가 우려돼요"),
    REASON_INCONVENIENT_SERVICE("서비스가 불편해요"),
    REASON_OTHER("기타"),
}
