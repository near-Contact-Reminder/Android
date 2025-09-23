package com.alarmy.near.data.mapper

import com.alarmy.near.data.entity.MemberInfoEntity
import com.alarmy.near.data.entity.WithdrawRequestEntity
import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.network.request.WithdrawRequest
import com.alarmy.near.presentation.feature.myprofile.model.LoginType
import com.alarmy.near.presentation.feature.myprofile.model.MyProfileInfoUIModel
import com.alarmy.near.presentation.feature.myprofile.model.WithdrawReason

/**
 * Data Layer Entity를 Model Layer로 변환
 */
fun MemberInfoEntity.toModel(): MemberInfo =
    MemberInfo(
        memberId = memberId,
        username = username,
        nickname = nickname,
        imageUrl = imageUrl,
        notificationAgreedAt = notificationAgreedAt,
        providerType = providerType,
    )

/**
 * Model Layer를 Data Layer Entity로 변환
 */
fun MemberInfo.toEntity(): MemberInfoEntity =
    MemberInfoEntity(
        memberId = memberId,
        username = username,
        nickname = nickname,
        imageUrl = imageUrl,
        notificationAgreedAt = notificationAgreedAt,
        providerType = providerType,
    )

/**
 * WithdrawReason을 Network Layer로 변환
 */
fun WithdrawReason.toRequest(customReason: String? = null): WithdrawRequest =
    WithdrawRequest(
        reasonType = this.name,
        customReason = customReason,
    )

fun WithdrawRequest.toEntity(): WithdrawRequestEntity =
    WithdrawRequestEntity(
        reasonType = reasonType,
        customReason = customReason,
    )

/**
 * Model 계층 모델을 UI 계층 모델로 변환
 */
fun MemberInfo.toMyProfileInfoUIModel(): MyProfileInfoUIModel =
    MyProfileInfoUIModel(
        nickname = nickname,
        imageUrl = imageUrl,
        notificationAgreedAt = notificationAgreedAt,
        providerType = mapProviderType(providerType),
    )

/**
 * ProviderType 문자열을 LoginType enum으로 변환
 */
private fun mapProviderType(providerType: String): LoginType =
    when (providerType.uppercase()) {
        "KAKAO" -> LoginType.KAKAO
        else -> LoginType.ETC
    }
