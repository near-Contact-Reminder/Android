package com.alarmy.near.data.source

import com.alarmy.near.model.ProviderType

/**
 * 소셜 로그인 데이터 소스 인터페이스
 */
interface SocialLoginDataSource {
    val supportedType: ProviderType

    suspend fun login(): Result<String>
}
