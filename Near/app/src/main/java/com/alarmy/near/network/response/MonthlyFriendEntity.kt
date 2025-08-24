package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyFriendEntity(
    val friendId: String,
    val name: String,
    val type: String,
    val nextContactAt: String,
)
