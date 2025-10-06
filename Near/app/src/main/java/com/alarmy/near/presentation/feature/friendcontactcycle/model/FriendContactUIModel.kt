package com.alarmy.near.presentation.feature.friendcontactcycle.model

import android.os.Parcelable
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.model.contact.Contact
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendContactUIModel(
    val id: Long,
    val name: String,
    val photoUri: String? = null,
    val phones: List<String> = emptyList(),
    val birthDay: String? = null,
    val memo: String? = null,
    val reminderInterval: ReminderInterval? = null,
) : Parcelable

/**
 * Contact를 FriendContactUIModel로 변환
 */
fun Contact.toFriendContactUIModel(): FriendContactUIModel =
    FriendContactUIModel(
        id = id,
        name = name,
        photoUri = photoUri,
        phones = phones,
        birthDay = birthDay,
        memo = memo,
    )
