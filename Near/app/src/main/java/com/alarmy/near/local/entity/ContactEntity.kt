package com.alarmy.near.local.entity

data class ContactEntity(
    val id: Long,
    val displayName: String,
    val phoneNumbers: List<String>,
)
