package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    val name: String,
    val relation: String,
    val contactFrequency: ContactFrequencyRequest,
    val birthday: String?,
    val anniversaryList: List<AnniversaryRequest>,
    val memo: String?,
    val phone: String?,
)

@Serializable
data class ContactFrequencyRequest(
    val contactWeek: String,
    val dayOfWeek: String,
)

@Serializable
data class AnniversaryRequest(
    val id: Int,
    val title: String,
    val date: String,
)
