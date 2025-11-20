package com.alarmy.near.presentation.preview.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.preview.model.DevicePreviewSpec
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * 지정된 기기 해상도를 기반으로 프리뷰 캔버스를 구성하고
 * 전달받은 컴포저블을 그 안에 렌더링합니다.
 * 모든 화면에서 동일한 프리뷰 규격을 재사용할 때 활용할 수 있습니다.
 */
@Composable
fun DevicePreviewFrame(
    spec: DevicePreviewSpec,
    content: @Composable () -> Unit,
) {
    NearTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 기기 정보 라벨
            Text(
                text = "${spec.deviceName} (${spec.widthDp}dp x ${spec.heightDp}dp)",
                style = NearTheme.typography.B2_14_MEDIUM,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            val aspectRatio = spec.widthDp.toFloat() / spec.heightDp.toFloat()
            // 프리뷰
            Surface(
                modifier =
                    Modifier
                        .widthIn(max = spec.widthDp.dp)
                        .heightIn(max = spec.heightDp.dp)
                        .aspectRatio(aspectRatio),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }
}
