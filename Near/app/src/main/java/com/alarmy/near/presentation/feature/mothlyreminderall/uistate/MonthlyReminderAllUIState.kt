package com.alarmy.near.presentation.feature.mothlyreminderall.uistate

import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel

sealed interface MonthlyReminderAllUIState {
    data object Loading : MonthlyReminderAllUIState

    data object Empty : MonthlyReminderAllUIState

    data class Success(
        val monthlyReminders: List<MonthlyReminderUIModel>,
        val completedReminders: List<MonthlyReminderUIModel>,
        val hasCompletedReminders: Boolean,
    ) : MonthlyReminderAllUIState
}
