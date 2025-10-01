package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * NearListModuleBackground
 *
 * 리스트 모듈의 배경 컴포넌트
 * - 연락처 아이콘과 "연락처에서 불러오기" 텍스트를 포함
 * - onClick: 클릭 시 실행할 액션 (연락처 권한 요청 등)
 */
@Composable
fun NearListModuleBackground(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                ).background(
                    color = NearTheme.colors.WHITE_FFFFFF,
                    shape = RoundedCornerShape(12.dp),
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .onNoRippleClick { onClick() }
                    .padding(vertical = 18.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.img_32_contact_square),
                    contentDescription = "연락처 아이콘",
                )

                Text(
                    text = "연락처에서 불러오기",
                    style = NearTheme.typography.B2_14_MEDIUM,
                )
            }

            content()
        }
    }
}

@Preview
@Composable
fun NearListModuleBackgroundPreview() {
    NearTheme {
        NearListModuleBackground()
    }
}
