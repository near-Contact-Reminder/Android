package com.alarmy.near.presentation.feature.mothlyreminderall.uistate

sealed interface MonthlyReminderAllUIEvent {
    data class ShowError(
        val throwable: Throwable?,
    ) : MonthlyReminderAllUIEvent

    data object RecordFriendShipSuccess : MonthlyReminderAllUIEvent
}
