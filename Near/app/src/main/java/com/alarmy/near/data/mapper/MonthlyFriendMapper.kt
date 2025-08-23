package com.alarmy.near.data.mapper

import com.alarmy.near.model.MonthlyFriend
import com.alarmy.near.network.response.MonthlyFriendEntity

fun MonthlyFriendEntity.toModel(): MonthlyFriend =
    MonthlyFriend(
        friendId = friendId,
        name = name,
        type = type,
        nextContactAt = nextContactAt,
    )
