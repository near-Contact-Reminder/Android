package com.alarmy.near.presentation.feature.friendprofile.uistate

import com.alarmy.near.model.FriendRecord

data class FriendShipRecordState(
    val records: List<FriendRecord> = emptyList(),
    val isEmpty: Boolean = true,
    val isLoading: Boolean = false,
)
