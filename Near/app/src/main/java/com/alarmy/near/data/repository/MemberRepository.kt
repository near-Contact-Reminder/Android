package com.alarmy.near.data.repository

import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.member.WithdrawRequest
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    // 현재 로그인한 회원의 정보를 조회
    fun getMyInfo(): Flow<MemberInfo>
    
    // 회원 탈퇴
    fun withdraw(request: WithdrawRequest): Flow<Unit>
}
