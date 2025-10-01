package com.alarmy.near.presentation.feature.friendcontactcycle.state

import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.model.contact.Contact

/**
 * FriendContactCycle 화면의 UI 이벤트
 */
sealed class FriendContactUIEvent {
    // 화면 분기 관련 이벤트
    object MoveToNextStep : FriendContactUIEvent()

    object MoveToPreviousStep : FriendContactUIEvent()

    // 연락처 관련 이벤트
    data class AddSelectedContacts(val contacts: List<Contact>) : FriendContactUIEvent()
    
    data class DeselectContact(val contactId: String) : FriendContactUIEvent()

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
