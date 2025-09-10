package com.alarmy.near.data.mapper

import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.presentation.feature.myprofile.model.LoginType
import com.alarmy.near.presentation.feature.myprofile.model.MyProfileInfo

/**
 * Member 관련 데이터 변환 매퍼
 * Data 계층 모델을 Presentation 계층 모델로 변환
 */
fun MemberInfo.toMyProfileInfo(): MyProfileInfo =
    MyProfileInfo(
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
