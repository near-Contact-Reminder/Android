package com.alarmy.near.presentation.feature.login.model

/**
 * 약관 항목 정보
 */
data class TermsItem(
    val title: String,
    val termType: TermType,
    val isAgreed: Boolean,
)
