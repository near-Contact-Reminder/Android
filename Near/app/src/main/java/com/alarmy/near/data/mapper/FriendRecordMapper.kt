package com.alarmy.near.data.mapper

import com.alarmy.near.model.FriendRecord
import com.alarmy.near.network.response.FriendRecordEntity

fun FriendRecordEntity.toModel(): FriendRecord =
    FriendRecord(
        isChecked = isChecked,
        createdAt = createdAt,
    )
