package com.alarmy.near.data.mapper

import com.alarmy.near.model.friendsummary.ContactFrequencyLevel
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.network.response.FriendSummaryEntity

private val CONTACT_FREQUENCY_LOW_RANGE = 0..29
private val CONTACT_FREQUENCY_MIDDLE_RANGE = 30..69
private val CONTACT_FREQUENCY_HIGH_RANGE = 70..100

fun FriendSummaryEntity.toModel(): FriendSummary =
    FriendSummary(
        id = friendId,
        name = name,
        profileImageUrl = imageUrl,
        lastContactedAt = lastContactAt,
        isContacted = true,
        contactFrequencyLevel =
            when (checkRate) {
                in CONTACT_FREQUENCY_LOW_RANGE -> ContactFrequencyLevel.LOW
                in CONTACT_FREQUENCY_MIDDLE_RANGE -> ContactFrequencyLevel.MIDDLE
                in CONTACT_FREQUENCY_HIGH_RANGE -> ContactFrequencyLevel.HIGH
                else -> ContactFrequencyLevel.LOW
            },
    )
