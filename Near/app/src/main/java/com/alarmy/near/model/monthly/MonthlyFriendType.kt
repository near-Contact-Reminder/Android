package com.alarmy.near.model.monthly

import androidx.annotation.DrawableRes
import com.alarmy.near.R

enum class MonthlyFriendType(
    @param:DrawableRes val imageSrc: Int,
) {
    ANNIVERSARY(R.drawable.icon_visual_24_heart),
    BIRTHDAY(R.drawable.icon_visual_cake),
    MESSAGE(R.drawable.icon_visual_mail),
    ;

    companion object {
        private const val ERROR_MESSAGE_NOT_FOUND_MONTHLY_TYPE = "일치하는 타입이 없습니다"

        fun from(value: String): MonthlyFriendType =
            runCatching { valueOf(value.uppercase()) }.getOrNull() ?: throw IllegalStateException(
                ERROR_MESSAGE_NOT_FOUND_MONTHLY_TYPE,
            )
    }
}
