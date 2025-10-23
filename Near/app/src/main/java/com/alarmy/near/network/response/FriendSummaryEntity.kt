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
    val checkRate: Int = 0, // TODO #Issue: 52 Default 값 추후 제거 필요
    val lastContactAt: String? = null,
)
