package com.alarmy.near.presentation.feature.friendcontactcycle.state

import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel

/**
 * FriendContactCycle 화면의 UI 상태
 */
data class FriendContactUIState(
    val currentStep: ContactCycleStep = ContactCycleStep.LOAD_CONTACTS,
    val contacts: List<FriendContactUIModel> = emptyList(),
    val isBulkSettingEnabled: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val selectedCycle: ReminderInterval? = null,
    val selectedContactId: String? = null, // 개별 설정 중인 contact ID
    val isLoading: Boolean = false,
    // 권한 다이얼로그 관련 상태
    val showPermissionDeniedDialog: Boolean = false,
    val onRequestPermission: (() -> Unit)? = null,
) {
    /**
     * 모든 연락처의 주기 설정이 완료되었는지 확인
     */
    val isAllContactsCycleSet: Boolean
        get() = contacts.isNotEmpty() && contacts.all { it.reminderInterval != null }
}
