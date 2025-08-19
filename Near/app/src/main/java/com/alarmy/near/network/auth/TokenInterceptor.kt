package com.alarmy.near.network.auth

import com.alarmy.near.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor
    @Inject
    constructor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            return chain.proceed(
                request =
                    request
                        .newBuilder()
                        .addHeader("Authorization", "Bearer ${BuildConfig.TEMP_TOKEN}")
                        .build(),
            )
        }
    }
