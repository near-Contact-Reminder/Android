package com.alarmy.near.data.mapper

import com.alarmy.near.model.Anniversary
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.Friend
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.network.request.AnniversaryRequest
import com.alarmy.near.network.request.ContactFrequencyRequest
import com.alarmy.near.network.request.FriendRequest
import com.alarmy.near.network.response.AnniversaryEntity
import com.alarmy.near.network.response.ContactFrequencyEntity
import com.alarmy.near.network.response.FriendEntity

fun FriendEntity.toModel(): Friend =
    Friend(
        friendId = friendId,
        imageUrl = imageUrl,
        relation = relation,
        name = name,
        contactFrequency = contactFrequency.toModel(),
        birthday = birthday,
        anniversaryList = anniversaryList.map { it.toModel() },
        memo = memo,
        phone = phone,
        lastContactAt = lastContactAt,
    )

fun ContactFrequencyEntity.toModel(): ContactFrequency =
    ContactFrequency(
        reminderInterval = ReminderInterval.valueOf(contactWeek),
        dayOfWeek = dayOfWeek,
    )

fun AnniversaryEntity.toModel(): Anniversary =
    Anniversary(
        id = id,
        title = title,
        date = date,
    )

fun Friend.toRequest(): FriendRequest =
    FriendRequest(
        name = name,
        relation = relation,
        contactFrequency = contactFrequency.toRequest(),
        birthday = birthday,
        anniversaryList = anniversaryList.map { it.toRequest() },
        memo = memo,
        phone = phone,
    )

fun ContactFrequency.toRequest(): ContactFrequencyRequest =
    ContactFrequencyRequest(
        contactWeek = reminderInterval.toString(),
        dayOfWeek = dayOfWeek,
    )

fun Anniversary.toRequest(): AnniversaryRequest =
    AnniversaryRequest(
        id = id,
        title = title,
        date = date,
    )
