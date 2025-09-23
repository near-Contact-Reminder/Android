package com.alarmy.near.data.repository

import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.member.WithdrawRequest
import com.alarmy.near.network.service.MemberApiService
import com.alarmy.near.utils.extensions.apiCallFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 회원 정보 Repository 구현체
 */
@Singleton
class MemberRepositoryImpl
    @Inject
    constructor(
        private val memberApiService: MemberApiService,
    ) : MemberRepository {
        // 현재 로그인한 회원의 정보를 조회
        override fun getMyInfo(): Flow<MemberInfo> =
            apiCallFlow {
                memberApiService.getMyInfo()
            }

        // 회원 탈퇴
        override fun withdraw(request: WithdrawRequest): Flow<Unit> =
            apiCallFlow {
                memberApiService.withdraw(request)
            }
    }
