package com.alarmy.near.network.service

import com.alarmy.near.model.member.MemberInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * 회원 관련 API 서비스
 */
interface MemberApiService {
    // 현재 로그인한 회원의 정보를 조회
    @GET("member/me")
    suspend fun getMyInfo(
        @Header("Authorization") authorization: String,
    ): Response<MemberInfo>
}
