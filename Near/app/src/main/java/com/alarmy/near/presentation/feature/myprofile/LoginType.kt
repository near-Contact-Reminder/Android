package com.alarmy.near.presentation.feature.myprofile

import androidx.annotation.StringRes
import com.alarmy.near.R

enum class LoginType(
    val typeTitle: String,
    @StringRes val logoTitle: Int? = null,
) {
    KAKAO("카카오", R.drawable.ic_kakao_badge_32),
    ETC("-"),
}
