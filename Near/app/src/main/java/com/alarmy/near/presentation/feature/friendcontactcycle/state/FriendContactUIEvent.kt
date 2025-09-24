package com.alarmy.near.presentation.feature.friendcontactcycle.state

import com.alarmy.near.model.ReminderInterval

/**
 * FriendContactCycle 화면의 UI 이벤트
 */
sealed class FriendContactUIEvent {
    // 화면 분기 관련 이벤트
    object MoveToNextStep : FriendContactUIEvent()

    object MoveToPreviousStep : FriendContactUIEvent()

    // 연락처 관련 이벤트 (ContactLoadContent에서 처리)
    object LoadContacts : FriendContactUIEvent()

    // 주기 설정 관련 이벤트 (ContactCycleContent에서 처리)
    object ToggleBulkSetting : FriendContactUIEvent()

    object OpenBottomSheet : FriendContactUIEvent()

    object CloseBottomSheet : FriendContactUIEvent()

    // 주기 설정 (CycleSettingBottomSheet 에서 처리)
    data class CompleteCycleSetting(
        val reminderInterval: ReminderInterval,
    ) : FriendContactUIEvent()

    data class SetContactCycle(
        val contactId: String,
        val reminderInterval: ReminderInterval,
    ) : FriendContactUIEvent()
}
