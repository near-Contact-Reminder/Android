package com.alarmy.near.presentation.feature.myprofile.model

data class MyProfileInfoUIModel(
    val nickname: String,
    val imageUrl: String?,
    val notificationAgreedAt: String?,
    val providerType: LoginType,
)
