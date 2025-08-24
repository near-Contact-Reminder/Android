package com.alarmy.near.model

import androidx.compose.runtime.Immutable

@Immutable
data class FriendSummary(
    val id: String,
    val name: String,
    val profileImageUrl: String?,
    val lastContactedAt: String?,
    val isContacted: Boolean,
    val contactFrequency: ContactFrequency,
)
