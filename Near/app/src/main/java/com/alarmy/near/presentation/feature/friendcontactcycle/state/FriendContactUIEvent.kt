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
    data class AddSelectedContacts(
        val contacts: List<Contact>,
    ) : FriendContactUIEvent()

    data class DeselectContact(
        val contactId: String,
    ) : FriendContactUIEvent()

    // 주기 설정 관련 이벤트 (ContactCycleContent에서 처리)
    object ToggleBulkSetting : FriendContactUIEvent()

    object OpenBottomSheet : FriendContactUIEvent()

    // 개별 연락처 주기 설정 바텀시트 열기
    data class OpenIndividualBottomSheet(
        val contactId: String,
    ) : FriendContactUIEvent()

    object CloseBottomSheet : FriendContactUIEvent()

    // 주기 설정 (CycleSettingBottomSheet 에서 처리)
    data class CompleteCycleSetting(
        val reminderInterval: ReminderInterval,
    ) : FriendContactUIEvent()

    // 친구 초기 설정 완료
    object CompleteFriendInit : FriendContactUIEvent()

    // 홈 화면으로 이동
    object NavigateToHome : FriendContactUIEvent()
}
