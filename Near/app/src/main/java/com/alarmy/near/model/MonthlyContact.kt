package com.alarmy.near.model

data class MonthlyContact(
    val friendId: String,
    val name: String,
    val type: String,
    val nextContactAt: String,
)
