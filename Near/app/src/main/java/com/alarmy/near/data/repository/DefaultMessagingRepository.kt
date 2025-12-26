package com.alarmy.near.data.repository

import com.alarmy.near.network.request.FcmTokenRegistrationRequest
import com.alarmy.near.network.request.FcmTokenUnRegistrationRequest
import com.alarmy.near.network.service.MessagingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultMessagingRepository
    @Inject
    constructor(
        private val messagingService: MessagingService,
    ) : MessagingRepository {
        override fun registerToken(token: String): Flow<Unit> =
            // token 등록 실패시는 로그인 실패로 볼까?
            flow {
                messagingService.registerToken(
                    FcmTokenRegistrationRequest(
                        token = token,
                        osType = OS_TYPE,
                    ),
                )
            }

        override fun unregisterToken(token: String): Flow<Unit> =
            flow {
                messagingService.unregisterToken(FcmTokenUnRegistrationRequest(token = token))
        }

        companion object {
            private const val OS_TYPE = "ANDROID"
        }
    }
