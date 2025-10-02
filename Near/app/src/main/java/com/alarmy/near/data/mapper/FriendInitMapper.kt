package com.alarmy.near.data.mapper

import com.alarmy.near.model.Anniversary
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.DayOfWeek
import com.alarmy.near.model.Friend
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.network.request.ContactFrequencyInitRequest
import com.alarmy.near.network.request.FriendInitItemRequest
import com.alarmy.near.network.response.AnniversaryInitEntity
import com.alarmy.near.network.response.ContactFrequencyInitEntity
import com.alarmy.near.network.response.FriendInitItemEntity
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.utils.extensions.DateExtension

/**
 * 010 전화번호를 010-0000-0000 형태로 포맷팅하는 함수
 * - //010-0000-0000 으로 앞에 //가 붙는 경우
 * - 01012341234로 하이픈이 없는 경우
 * 최종적으로 010-0000-0000 형태로 변환합니다.
 */
private fun String.formatPhoneNumber(): String {
    // 1. 불필요한 문자들 제거
    val cleaned =
        this
            .replace("//", "") // // 제거
            .replace("-", "") // 하이픈 제거 (하이픈이 없는 경우 위치값으로 넣어줘야 하기때문에 우선 제거)
            .replace(" ", "") // 공백 제거
            .trim()

    // 2. 000-0000-0000 형태로 포맷팅
    return "${cleaned.substring(0, 3)}-${cleaned.substring(3, 7)}-${cleaned.substring(7)}"
}

/**
 * UI 모델을 서버 요청 모델로 변환
 */
fun FriendContactUIModel.toFriendInitItemRequest(providerType: String): FriendInitItemRequest =
    FriendInitItemRequest(
        name = name,
        phone = phones.first().formatPhoneNumber(),
        memo = memo,
        birthDay = birthDay,
        source = providerType,
        contactFrequency = createContactFrequencyRequest(reminderInterval!!),
        imageUploadRequest = null,
        anniversary = null,
        relation = "FRIEND", // 기본값으로 FRIEND 설정
    )

/**
 * ContactFrequencyInitEntity를 모델로 변환
 */
fun ContactFrequencyInitEntity.toModel(): ContactFrequency =
    ContactFrequency(
        reminderInterval = ReminderInterval.valueOf(contactWeek),
        dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
    )

/**
 * AnniversaryInitEntity를 모델로 변환
 */
fun AnniversaryInitEntity.toModel(): Anniversary =
    Anniversary(
        id = id,
        title = title,
        date = date,
    )

/**
 * 서버 응답을 모델로 변환
 */
fun FriendInitItemEntity.toModel(): Friend =
    Friend(
        friendId = friendId,
        name = name,
        phone = phone,
        memo = memo,
        birthday = null,
        imageUrl = preSignedImageUrl,
        relation = Relation.valueOf(source),
        contactFrequency = contactFrequency.toModel(),
        anniversaryList = anniversary?.let { listOf(it.toModel()) } ?: emptyList(),
        lastContactAt = nextContactAt,
    )

/**
 * ReminderInterval을 ContactFrequencyInitRequest로 변환
 */
private fun createContactFrequencyRequest(reminderInterval: ReminderInterval): ContactFrequencyInitRequest =
    ContactFrequencyInitRequest(
        contactWeek = DateExtension.toContactWeekString(reminderInterval),
        dayOfWeek = DateExtension.getTodayDayOfWeekInEnglish(),
    )
