package com.alarmy.near.local.entity

data class ContactEntity(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val photoUri: String?, // 사진
    val birthDay: String?, // 생일
    val memo: String?, // 메모
    val groups: List<String>, // 그룹 (ex. 가족, 친구, 회사)
    val importantDates: List<ImportantDate>,
)

data class ImportantDate(
    val label: String,
    val date: String,
)
