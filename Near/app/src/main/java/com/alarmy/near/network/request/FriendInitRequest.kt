package com.alarmy.near.network.request

import kotlinx.serialization.Serializable

/**
 * 친구 초기 설정 API 요청 데이터 모델
 */
@Serializable
data class FriendInitRequest(
    val friendList: List<FriendInitItemRequest>,
)

/**
 * 친구 초기 설정 개별 아이템 요청 데이터 모델
 */
@Serializable
data class FriendInitItemRequest(
    val name: String,
    val phone: String,
    val memo: String? = null,
    val birthDay: String? = null,
    val source: String,
    val contactFrequency: ContactFrequencyInitRequest,
    val imageUploadRequest: ImageUploadRequest? = null,
    val anniversary: AnniversaryInitRequest? = null,
    val relation: String? = null,
)

/**
 * 연락처 주기 설정 요청 데이터 모델
 */
@Serializable
data class ContactFrequencyInitRequest(
    val contactWeek: String, // EVERY_WEEK, EVERY_TWO_WEEKS, EVERY_MONTH
    val dayOfWeek: String, // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
)

/**
 * 이미지 업로드 요청 데이터 모델
 */
@Serializable
data class ImageUploadRequest(
    val fileName: String? = null,
    val contentType: String? = null,
    val fileSize: Int? = null,
    val category: String? = null,
)

/**
 * 기념일 요청 데이터 모델
 */
@Serializable
data class AnniversaryInitRequest(
    val title: String,
    val date: String? = null,
)
