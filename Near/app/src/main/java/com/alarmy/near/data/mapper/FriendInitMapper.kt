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
import com.alarmy.near.utils.PhoneNumberFormatter
import com.alarmy.near.utils.extensions.DateExtension
import com.alarmy.near.utils.logger.NearLog

/**
 * PhoneNumberFormatter를 사용하여 전화번호를 포맷팅하는 함수
 * - 다양한 전화번호 형식 지원 (휴대폰, 지역번호 등)
 * - 한국 전화번호 형식으로 통일 (010-0000-0000, 02-0000-0000 등)
 * - 잘못된 형식의 경우 원본 반환
 */
private fun String.formatPhoneNumber(): String = PhoneNumberFormatter.formatPhoneNumber(this)

/**
 * UI 모델을 서버 요청 모델로 변환
 */
fun FriendContactUIModel.toFriendInitItemRequest(providerType: String): FriendInitItemRequest =
    FriendInitItemRequest(
        name = name,
        phone = phones.firstOrNull()?.formatPhoneNumber() ?: "",
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
        reminderInterval = contactWeek.toReminderInterval(),
        dayOfWeek = dayOfWeek.toDayOfWeek(),
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
        relation = source.toRelation(),
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

private fun String.toReminderInterval(): ReminderInterval =
    runCatching { ReminderInterval.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 연락 주기 값: '$this', 기본값(EVERY_WEEK) 사용")
        }.getOrDefault(ReminderInterval.EVERY_WEEK)

private fun String.toDayOfWeek(): DayOfWeek =
    runCatching { DayOfWeek.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 요일 값: '$this', 기본값(MONDAY) 사용")
        }.getOrDefault(DayOfWeek.MONDAY)

private fun String.toRelation(): Relation =
    runCatching { Relation.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 관계 값: '$this', 기본값(FRIEND) 사용")
        }.getOrDefault(Relation.FRIEND)
