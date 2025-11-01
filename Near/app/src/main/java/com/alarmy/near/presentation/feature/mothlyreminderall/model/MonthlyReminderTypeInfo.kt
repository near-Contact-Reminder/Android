package com.alarmy.near.presentation.feature.mothlyreminderall.model

import androidx.annotation.DrawableRes
import com.alarmy.near.R
import com.alarmy.near.model.monthly.MonthlyFriendType

enum class MonthlyReminderTypeInfo(
    @param:DrawableRes val imageRes: Int,
    val description: String,
) {
    ANNIVERSARY(
        imageRes = R.drawable.icon_visual_24_heart,
        description = "소중한 날 마음을 전해요",
    ),
    BIRTHDAY(
        imageRes = R.drawable.icon_visual_cake,
        description = "생일 축하 전해요",
    ),
    MESSAGE(
        imageRes = R.drawable.icon_visual_mail,
        description = "가볍게 안부인사 전해요",
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

