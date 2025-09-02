package com.alarmy.near.data.datasource

import com.alarmy.near.model.ProviderType

/**
 * 소셜 로그인 데이터 소스 인터페이스
 * Strategy 패턴으로 각 소셜 플랫폼별로 구현
 */
interface SocialLoginDataSource {
    /**
     * 지원하는 소셜 로그인 타입
     */
    val supportedType: ProviderType

    /**
     * 소셜 로그인 수행
     * Context는 생성자에서 주입받아 사용
     */
    suspend fun login(): Result<String>
}
