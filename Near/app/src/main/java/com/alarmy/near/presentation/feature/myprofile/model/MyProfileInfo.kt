package com.alarmy.near.presentation.feature.myprofile.model

data class MyProfileInfo(
    val nickname: String,
    val imageUrl: String?,
    val notificationAgreedAt: String?,
    val providerType: LoginType,
)
