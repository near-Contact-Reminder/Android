package com.alarmy.near.data.mapper

import com.alarmy.near.model.Friend
import com.alarmy.near.network.response.FriendEntity

fun FriendEntity.toModel(): Friend =
    Friend(
        friendId = friendId,
        position = position,
        source = source,
        name = name,
        imageUrl = imageUrl,
        fileName = fileName,
        checkRate = checkRate,
        lastContactAt = lastContactAt,
    )
