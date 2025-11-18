package com.alarmy.near.presentation.feature.mothlyreminderall.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MonthlyReminderUIModel(
    val friendId: String,
    val name: String,
    @DrawableRes val imageRes: Int,
    @StringRes val descriptionRes: Int,
    val nextContactAt: String,
    val daysUntilNextContact: String,
    val isToday: Boolean,
)

