package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toEntity
import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.data.mapper.toRequest
import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.network.service.MemberApiService
import com.alarmy.near.presentation.feature.myprofile.model.WithdrawReason
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
                memberApiService.getMyInfo().toModel()
            }

        // 회원 탈퇴
        override fun withdraw(
            reason: WithdrawReason,
            customReason: String?,
        ): Flow<Unit> =
            apiCallFlow {
                val request = reason.toRequest(customReason)
                memberApiService.withdraw(request.toEntity())
            }
    }
