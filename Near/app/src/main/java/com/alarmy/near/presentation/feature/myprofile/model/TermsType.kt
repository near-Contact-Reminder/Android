package com.alarmy.near.presentation.feature.myprofile.model

import com.alarmy.near.BuildConfig

/**
 * 약관 및 정책 타입
 */
enum class TermsType(
    val title: String,
    val url: String,
) {
    SERVICE_AGREED_TERMS(
        title = "서비스 이용약관",
        url = BuildConfig.SERVICE_AGREED_TERMS_URL,
    ),
    PERSONAL_INFO_TERMS(
        title = "개인정보 수집 및 이용동의",
        url = BuildConfig.PERSONAL_INFO_TERMS_URL,
    ),
    PRIVACY_POLICY_TERMS(
        title = "개인정보 처리방침",
        url = BuildConfig.PRIVACY_POLICY_TERMS_URL,
    ),
}
