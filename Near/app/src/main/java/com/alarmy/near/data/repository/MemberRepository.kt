package com.alarmy.near.data.repository

import com.alarmy.near.model.member.MemberInfo
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    // 현재 로그인한 회원의 정보를 조회
    fun getMyInfo(): Flow<MemberInfo>
}
