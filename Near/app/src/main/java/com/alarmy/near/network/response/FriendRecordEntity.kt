package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendRecordEntity(
    val isChecked: Boolean,
    val createdAt: String,
)
