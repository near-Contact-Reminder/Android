package com.alarmy.near.model

data class LoginResult(
    val isSuccess: Boolean,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val errorMessage: String? = null,
)
