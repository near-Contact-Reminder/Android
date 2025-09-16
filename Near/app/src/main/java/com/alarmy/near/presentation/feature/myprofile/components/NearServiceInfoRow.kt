package com.alarmy.near.presentation.feature.myprofile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearServiceInfoRow(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit = {},
    showDivider: Boolean = true,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .onNoRippleClick(onClick)
                .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 정보 라벨
        Text(
            text = label,
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.BLACK_1A1A1A,
        )

        // 화살표 아이콘
        Icon(
            painter = painterResource(R.drawable.ic_front_24_gray),
            tint = NearTheme.colors.GRAY01_888888,
            contentDescription = null,
        )
    }

    // 구분선 (선택사항)
    if (showDivider) {
        HorizontalDivider(
            color = NearTheme.colors.GRAY03_EBEBEB,
        )
    }
}
