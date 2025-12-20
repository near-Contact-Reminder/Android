package com.alarmy.near.network.service

import com.alarmy.near.network.request.FcmTokenRegistrationRequest
import com.alarmy.near.network.request.FcmTokenUnRegistrationRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface MessagingService {
    @POST("/messaging/register")
    suspend fun registerToken(
        @Body fcmTokenRegistrationRequest: FcmTokenRegistrationRequest,
    )

    @DELETE("/messaging/unregister")
    suspend fun unregisterToken(
        @Body fcmTokenUnRegistrationRequest: FcmTokenUnRegistrationRequest,
    )
}
