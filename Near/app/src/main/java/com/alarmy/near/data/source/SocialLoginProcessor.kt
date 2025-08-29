package com.alarmy.near.data.source

import android.content.Context
import com.alarmy.near.model.ProviderType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 소셜 로그인 프로세서
 * Strategy 패턴으로 동적으로 로그인 방식 선택
 */
@Singleton
class SocialLoginProcessor @Inject constructor(
    private val socialLoginDataSources: Set<@JvmSuppressWildcards SocialLoginDataSource>
) {
    
    /**
     * 소셜 로그인 처리
     * @param context Android Context
     * @param providerType 로그인 제공자 타입
     * @return 로그인 결과
     */
    suspend fun processLogin(
        context: Context,
        providerType: ProviderType
    ): Result<String> {
        val dataSource = socialLoginDataSources.find { it.supportedType == providerType }
            ?: return Result.failure(Exception("지원하지 않는 로그인 타입입니다: ${providerType.name}"))
        
        return dataSource.login(context)
    }
}
