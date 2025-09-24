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
    val isLoading: Boolean = false,
    val error: String? = null,
)
