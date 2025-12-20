package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRegistrationRequest(
    val token: String,
    val osType: String,
)
