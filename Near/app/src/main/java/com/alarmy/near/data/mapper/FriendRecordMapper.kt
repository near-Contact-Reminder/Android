package com.alarmy.near.data.mapper

import com.alarmy.near.model.FriendRecord
import com.alarmy.near.network.response.FriendRecordEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FriendRecordEntity.toModel(): FriendRecord =
    FriendRecord(
        isChecked = isChecked,
        createdAt = createdAt.toShortDate(),
    )

private fun String.toShortDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")

    val dateTime = LocalDateTime.parse(this, inputFormatter)
    return dateTime.format(outputFormatter)
}
