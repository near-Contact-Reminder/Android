package com.alarmy.near.network.service

import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.member.WithdrawRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header

/**
 * 회원 관련 API 서비스
 */
interface MemberApiService {
    // 현재 로그인한 회원의 정보를 조회
    @GET("member/me")
    suspend fun getMyInfo(): Response<MemberInfo>

    // 회원 탈퇴
    @HTTP(method = "DELETE", path = "member/withdraw", hasBody = true)
    suspend fun withdraw(
        @Body request: WithdrawRequest,
    ): Response<Unit>
}
