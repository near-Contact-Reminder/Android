package com.alarmy.near.presentation.feature.mothlyreminderall.model

import androidx.annotation.DrawableRes

data class MonthlyReminderUIModel(
    val friendId: String,
    val name: String,
    @DrawableRes val imageRes: Int,
    val description: String,
    val nextContactAt: String,
    val daysUntilNextContact: String,
)

