package com.alarmy.near.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.alarmy.near.presentation.preview.model.DevicePreviewSpec

/**
 * 삼성 대표 기기 해상도를 기반으로 Compose 프리뷰에서 사용할 dp 값을 제공합니다.
 */
class DevicePreviewParameterProvider : PreviewParameterProvider<DevicePreviewSpec> {
    override val values: Sequence<DevicePreviewSpec> =
        sequenceOf(
            DevicePreviewSpec(
                deviceName = "Galaxy S21 (FHD+)",
                widthDp = 360,
                heightDp = 800,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy S23 (FHD+)",
                widthDp = 360,
                heightDp = 780,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy S25 (QHD+)",
                widthDp = 412,
                heightDp = 915,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy S23 Ultra (QHD+)",
                widthDp = 411,
                heightDp = 915,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy Z Flip (FHD+)",
                widthDp = 360,
                heightDp = 860,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy Z Fold (QXGA+)",
                widthDp = 600,
                heightDp = 730,
            ),
            DevicePreviewSpec(
                deviceName = "Galaxy A60 (FHD+)",
                widthDp = 360,
                heightDp = 780,
            ),
        )
}
