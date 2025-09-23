package com.alarmy.near.presentation.feature.friendcontactcycle.model

enum class ContactCycleStep(
    val appbarTitle: String,
) {
    LOAD_CONTACTS("챙길 사람 불러오기"), // 연락처 불러오기 단계
    SET_CYCLE("챙김 주기 설정하기"), // 연락 주기 설정 단계
}
