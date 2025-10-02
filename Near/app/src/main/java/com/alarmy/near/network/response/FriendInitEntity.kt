package com.alarmy.near.network.response

import kotlinx.serialization.Serializable

/**
 * 친구 초기 설정 API 응답 데이터
 */
@Serializable
data class FriendInitEntity(
    val friendList: List<FriendInitItemEntity>,
)

@Serializable
data class FriendInitItemEntity(
    val friendId: String,
    val name: String,
    val source: String,
    val contactFrequency: ContactFrequencyInitEntity,
    val nextContactAt: String,
    val phone: String,
    val preSignedImageUrl: String? = null,
    val fileName: String? = null,
    val memo: String? = null,
    val anniversary: AnniversaryInitEntity? = null,
)

@Serializable
data class ContactFrequencyInitEntity(
    val contactWeek: String,
    val dayOfWeek: String,
)

@Serializable
data class AnniversaryInitEntity(
    val id: Int,
    val title: String,
    val date: String,
)
