package com.alarmy.near.model.contact

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val photoUri: String?, // 사진
    val birthDay: String?, // 생일
    val memo: String?, // 메모
) : Parcelable
