package com.alarmy.near.presentation.feature.friendcontactcycle.model

import com.alarmy.near.model.ReminderInterval

data class FriendContactUIModel(
    val id: Long,
    val name: String,
    val photoUri: String?,
    val reminderInterval: ReminderInterval? = null,
)
