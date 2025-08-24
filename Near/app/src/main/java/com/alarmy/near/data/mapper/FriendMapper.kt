package com.alarmy.near.data.mapper

import com.alarmy.near.model.ContactFrequencyLevel
import com.alarmy.near.model.FriendSummary
import com.alarmy.near.network.response.FriendSummaryEntity

fun FriendSummaryEntity.toModel(): FriendSummary =
    FriendSummary(
        id = friendId,
        name = name,
        profileImageUrl = imageUrl,
        lastContactedAt = lastContactAt,
        isContacted = true,
        contactFrequencyLevel =
            when (checkRate) {
                in 0..29 -> ContactFrequencyLevel.LOW
                in 30..69 -> ContactFrequencyLevel.MIDDLE
                in 70..100 -> ContactFrequencyLevel.HIGH
                else -> ContactFrequencyLevel.LOW
            },
    )
