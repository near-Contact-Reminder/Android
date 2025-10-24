package com.alarmy.near.presentation.feature.login.auth

// 소셜 로그인 제공자가 구현해야 하는 인터페이스
interface SocialLoginProvider {
    suspend fun performLogin(
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
    )
}
