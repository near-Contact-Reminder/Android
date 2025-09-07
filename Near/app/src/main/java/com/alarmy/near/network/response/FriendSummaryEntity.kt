package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendSummaryEntity(
    val friendId: String,
    val position: Int,
    val source: String,
    val name: String,
    val imageUrl: String? = null,
    val fileName: String? = null,
    val checkRate: Int,
    val lastContactAt: String? = null,
)
