package com.alarmy.near.presentation.feature.onboarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * 온보딩 페이지 데이터 모델
 * 각 페이지의 정보를 담는 데이터 클래스
 */
data class OnboardingPage(
    @StringRes val titleResId: Int,
    @DrawableRes val image: Int,
)
