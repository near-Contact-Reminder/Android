package com.alarmy.near.data.mapper

import com.alarmy.near.local.entity.ContactEntity
import com.alarmy.near.model.contact.Contact

fun ContactEntity.toModel(): Contact =
    Contact(
        id = id,
        name = name,
        phones = phones,
        photoUri = photoUri,
        birthDay = birthDay,
        memo = memo,
    )
