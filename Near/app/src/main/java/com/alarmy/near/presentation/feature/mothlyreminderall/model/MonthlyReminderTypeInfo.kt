package com.alarmy.near.presentation.feature.mothlyreminderall.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.alarmy.near.R
import com.alarmy.near.model.monthly.MonthlyFriendType

enum class MonthlyReminderTypeInfo(
    @param:DrawableRes val imageRes: Int,
    @param:StringRes val descriptionRes: Int,
) {
    ANNIVERSARY(
        imageRes = R.drawable.icon_visual_24_heart,
        descriptionRes = R.string.monthly_reminder_all_type_anniversary_description,
    ),
    BIRTHDAY(
        imageRes = R.drawable.icon_visual_cake,
        descriptionRes = R.string.monthly_reminder_all_type_birthday_description,
    ),
    MESSAGE(
        imageRes = R.drawable.icon_visual_mail,
        descriptionRes = R.string.monthly_reminder_all_type_message_description,
    ),
    ;

    companion object {
        fun from(type: MonthlyFriendType): MonthlyReminderTypeInfo =
            when (type) {
                MonthlyFriendType.ANNIVERSARY -> ANNIVERSARY
                MonthlyFriendType.BIRTHDAY -> BIRTHDAY
                MonthlyFriendType.MESSAGE -> MESSAGE
            }
    }
}

