package com.alarmy.near.data.repository

import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.member.WithdrawRequest
import com.alarmy.near.network.service.MemberApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
            flow {
                emit(
                    runCatching {
                        val response = memberApiService.getMyInfo("")

                        if (response.isSuccessful) {
                            response.body() ?: throw Exception("회원 정보가 null입니다.")
                        } else {
                            val errorMessage =
                                when (response.code()) {
                                    404 -> "해당 회원을 찾을 수 없습니다."
                                    401 -> "인증이 필요합니다."
                                    else -> "회원 정보 조회에 실패했습니다. (${response.code()})"
                                }
                            throw Exception(errorMessage)
                        }
                    }.getOrThrow(),
                )
            }

        // 회원 탈퇴
        override suspend fun withdraw(request: WithdrawRequest): Result<Unit> =
            runCatching {
                val response = memberApiService.withdraw("", request)

                if (response.isSuccessful) {
                    Unit
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "인증이 필요합니다."
                        404 -> "해당 회원을 찾을 수 없습니다."
                        else -> "회원 탈퇴에 실패했습니다. (${response.code()})"
                    }
                    throw Exception(errorMessage)
                }
            }
    }
