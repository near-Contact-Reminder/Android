package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenUnRegistrationRequest(
    val token: String,
)
