package com.alarmy.near.presentation.feature.friendcontactcycle.model

import com.alarmy.near.R

enum class ContactCycleStep(
    val appbarTitleResId: Int,
) {
    LOAD_CONTACTS(R.string.friend_contact_cycle_load_contacts_title), // 연락처 불러오기 단계
    SET_CYCLE(R.string.friend_contact_cycle_set_cycle_title), // 연락 주기 설정 단계
}
