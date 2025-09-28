package com.alarmy.near.presentation.feature.login.model

import androidx.annotation.StringRes
import com.alarmy.near.BuildConfig
import com.alarmy.near.R

/**
 * 약관 및 정책 타입
 */
enum class TermType(
    @StringRes val titleRes: Int,
    val url: String,
) {
    SERVICE_TERMS(
        titleRes = R.string.terms_service_agreed,
        url = BuildConfig.SERVICE_AGREED_TERMS_URL,
    ),
    PRIVACY_COLLECTION(
        titleRes = R.string.terms_personal_info,
        url = BuildConfig.PERSONAL_INFO_TERMS_URL,
    ),
    PRIVACY_POLICY(
        titleRes = R.string.terms_privacy_policy,
        url = BuildConfig.PRIVACY_POLICY_TERMS_URL,
    ),
}
