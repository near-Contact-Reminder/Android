package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendEntity(
    val friendId: String,
    val imageUrl: String,
    val relation: String,
    val name: String,
    val contactFrequencyEntity: ContactFrequencyEntity,
    val birthday: String?,
    val anniversaryEntityList: List<AnniversaryEntity>,
    val memo: String?,
    val phone: String?,
    val lastContactAt: String?,
)

@Serializable
data class ContactFrequencyEntity(
    val contactWeek: String,
    val dayOfWeek: String,
)

@Serializable
data class AnniversaryEntity(
    val id: Int,
    val title: String,
    val date: String,
)
