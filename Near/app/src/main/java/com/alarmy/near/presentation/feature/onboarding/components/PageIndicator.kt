package com.alarmy.near.presentation.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * 페이지 인디케이터 컴포넌트
 * 현재 페이지를 시각적으로 표시하는 점들
 */
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) NearTheme.colors.BLUE01_5AA2E9 else NearTheme.colors.GRAY03_EBEBEB,
                        ),
            )
        }
    }
}

@Preview
@Composable
fun PageIndicatorPreview() {
    NearTheme {
        PageIndicator(pageCount = 3, currentPage = 1)
    }
}
