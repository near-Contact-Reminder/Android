package com.alarmy.near.data.mapper

import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.model.monthly.MonthlyFriendType
import com.alarmy.near.network.response.MonthlyFriendEntity

fun MonthlyFriendEntity.toModel(): MonthlyFriend =
    MonthlyFriend(
        friendId = friendId,
        name = name,
        type = MonthlyFriendType.from(type),
        nextContactAt = nextContactAt,
    )
