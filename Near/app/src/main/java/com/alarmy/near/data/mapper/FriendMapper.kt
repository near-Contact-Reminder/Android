package com.alarmy.near.data.mapper

import com.alarmy.near.model.Anniversary
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.DayOfWeek
import com.alarmy.near.model.Friend
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.network.request.AnniversaryRequest
import com.alarmy.near.network.request.ContactFrequencyRequest
import com.alarmy.near.network.request.FriendRequest
import com.alarmy.near.network.response.AnniversaryEntity
import com.alarmy.near.network.response.ContactFrequencyEntity
import com.alarmy.near.network.response.FriendEntity
import com.alarmy.near.utils.logger.NearLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun FriendEntity.toModel(): Friend =
    Friend(
        friendId = friendId,
        imageUrl = imageUrl,
        relation = relation.toRelation(),
        name = name,
        contactFrequency = contactFrequency.toModel(),
        birthday = birthday,
        anniversaryList = anniversaryList.map { it.toModel() },
        memo = memo,
        phone = phone,
        isContactToday = lastContactAt?.isToday() ?: false,
        lastContactAt = lastContactAt,
        lastContactFormat = lastContactAt?.contactFormat()
    )

fun String.contactFormat(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("M월 d일")

    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}

private fun String.isToday(): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
    val targetDate = LocalDate.parse(this, formatter)
    val today = LocalDate.now()
    return targetDate == today
}

fun ContactFrequencyEntity.toModel(): ContactFrequency =
    ContactFrequency(
        reminderInterval = contactWeek.toReminderInterval(),
        dayOfWeek = dayOfWeek.toDayOfWeek(),
    )

fun AnniversaryEntity.toModel(): Anniversary =
    Anniversary(
        id = id,
        title = title,
        date = date,
    )

fun Friend.toRequest(): FriendRequest =
    FriendRequest(
        name = name,
        relation = relation.toString(),
        contactFrequency = contactFrequency.toRequest(),
        birthday = birthday,
        anniversaryList = anniversaryList.map { it.toRequest() },
        memo = memo,
        phone = phone,
    )

fun ContactFrequency.toRequest(): ContactFrequencyRequest =
    ContactFrequencyRequest(
        contactWeek = reminderInterval.toString(),
        dayOfWeek = dayOfWeek.toString(),
    )

fun Anniversary.toRequest(): AnniversaryRequest =
    AnniversaryRequest(
        id = id,
        title = title,
        date = date,
    )

/**
 * 안전한 ReminderInterval 변환 (로깅 포함)
 */
private fun String.toReminderInterval(): ReminderInterval =
    runCatching { ReminderInterval.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 연락 주기 값: '$this', 기본값(EVERY_WEEK) 사용")
        }.getOrDefault(ReminderInterval.EVERY_WEEK)

/**
 * 안전한 DayOfWeek 변환 (로깅 포함)
 */
private fun String.toDayOfWeek(): DayOfWeek =
    runCatching { DayOfWeek.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 요일 값: '$this', 기본값(MONDAY) 사용")
        }.getOrDefault(DayOfWeek.MONDAY)

/**
 * 안전한 Relation 변환 (로깅 포함)
 */
private fun String.toRelation(): Relation =
    runCatching { Relation.valueOf(this) }
        .onFailure { exception ->
            NearLog.w("잘못된 관계 값: '$this', 기본값(FRIEND) 사용")
        }.getOrDefault(Relation.FRIEND)
