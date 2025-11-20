package com.alarmy.near.presentation.preview.model

/**
 * 다양한 기기 해상도를 위한 공용 프리뷰 스펙입니다.
 * Compose 프리뷰에서 여러 화면 크기를 쉽게 테스트할 때 사용합니다.
 */
data class DevicePreviewSpec(
    val deviceName: String,
    val widthDp: Int,
    val heightDp: Int,
)
