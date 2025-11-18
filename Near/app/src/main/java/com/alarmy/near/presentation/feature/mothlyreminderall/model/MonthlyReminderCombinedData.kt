package com.alarmy.near.presentation.feature.mothlyreminderall.model

import com.alarmy.near.model.monthly.MonthlyFriend

data class MonthlyReminderCombinedData(
    val monthlyFriends: List<MonthlyFriend>,
    val completeFriends: List<MonthlyFriend>,
)
