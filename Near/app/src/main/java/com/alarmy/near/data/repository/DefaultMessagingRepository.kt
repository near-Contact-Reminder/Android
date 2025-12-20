package com.alarmy.near.data.repository

import com.alarmy.near.network.request.FcmTokenRegistrationRequest
import com.alarmy.near.network.request.FcmTokenUnRegistrationRequest
import com.alarmy.near.network.service.MessagingService
import javax.inject.Inject

class DefaultMessagingRepository
    @Inject
    constructor(
        private val messagingService: MessagingService,
    ) : MessagingRepository {
        override fun registerToken(token: String) {
            messagingService.registerToken(
                FcmTokenRegistrationRequest(
                    token = token,
                    osType = OS_TYPE,
                ),
            )
        }

        override fun unregisterToken(token: String) {
            messagingService.unregisterToken(FcmTokenUnRegistrationRequest(token = token))
        }

        companion object {
            private const val OS_TYPE = "ANDROID"
        }
    }
