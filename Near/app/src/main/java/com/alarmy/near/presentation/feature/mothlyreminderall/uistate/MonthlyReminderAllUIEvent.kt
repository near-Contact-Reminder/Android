package com.alarmy.near.presentation.feature.mothlyreminderall.uistate

sealed interface MonthlyReminderAllUIEvent {
    data object NetworkError : MonthlyReminderAllUIEvent
}

