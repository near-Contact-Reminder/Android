package com.alarmy.near.data.mapper

import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.FriendSummary
import com.alarmy.near.network.response.FriendEntity

fun FriendEntity.toModel(): FriendSummary =
    FriendSummary(
        id = friendId,
        name = name,
        profileImageUrl = imageUrl,
        lastContactedAt = lastContactAt,
        isContacted = true,
        contactFrequency =
            when (checkRate) {
                in 0..29 -> ContactFrequency.LOW
                in 30..69 -> ContactFrequency.MIDDLE
                in 70..100 -> ContactFrequency.HIGH
                else -> ContactFrequency.LOW
        },
            )
