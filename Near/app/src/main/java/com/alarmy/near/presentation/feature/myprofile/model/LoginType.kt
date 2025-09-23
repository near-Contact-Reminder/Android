package com.alarmy.near.presentation.feature.myprofile.model

import androidx.annotation.StringRes
import com.alarmy.near.R

enum class LoginType(
    @StringRes val typeTitleRes: Int,
    @StringRes val loginTypeImage: Int? = null,
) {
    KAKAO(R.string.login_type_kakao, R.drawable.ic_kakao_badge_32),
    ETC(R.string.login_type_etc),
}
