package com.alarmy.near.model

data class Friend(
    val friendId: String,
    val imageUrl: String,
    val relation: String,
    val name: String,
    val contactFrequency: ContactFrequency,
    val birthday: String?,
    val anniversaryList: List<Anniversary>,
    val memo: String?,
    val phone: String?,
    val lastContactAt: String?,
)

data class ContactFrequency(
    val contactWeek: String,
    val dayOfWeek: String,
)

data class Anniversary(
    val id: Int,
    val title: String,
    val date: String,
)
