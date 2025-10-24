package com.alarmy.near.presentation.feature.login.auth

import android.content.Context
import com.alarmy.near.model.ProviderType

/**
 * 소셜 로그인을 통합 관리하는 핸들러
 */
class SocialLoginHandler(
    private val context: Context,
) {
    // 소셜 로그인 제공자들을 Map으로 관리
    // 새로운 로그인 타입 추가 시 이 부분 수정
    private val loginProviders: Map<ProviderType, SocialLoginProvider> by lazy {
        mapOf(
            ProviderType.KAKAO to KakaoLoginManager(context),
            // TODO: ProviderType.APPLE to AppleLoginManager(context),
            // TODO: ProviderType.GOOGLE to GoogleLoginManager(context),
        )
    }

    // 소셜 로그인 수행
    suspend fun performSocialLogin(
        providerType: ProviderType,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        val provider =
            loginProviders[providerType]
                ?: throw IllegalArgumentException("지원하지 않는 로그인 제공자: $providerType")

        provider.performLogin(onSuccess, onFailure)
    }
}
