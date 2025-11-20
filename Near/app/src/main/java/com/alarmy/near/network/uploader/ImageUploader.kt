package com.alarmy.near.network.uploader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploader
    @Inject
    constructor(
        private val okHttpClient: OkHttpClient,
    ) {

        suspend fun upload(
            url: String,
            contentType: String,
            data: ByteArray,
        ) = withContext(Dispatchers.IO) {
            val requestBody = data.toRequestBody(contentType.toMediaTypeOrNull())
            val request =
                Request
                    .Builder()
                    .url(url)
                    .put(requestBody)
                    .build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IllegalStateException("이미지 업로드에 실패했습니다.")
                }
            }
        }
    }
