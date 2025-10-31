package com.alarmy.near.presentation.feature.mothlyreminderall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun MonthlyReminderComplete() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NearTheme.colors.BG02_F4F9FD)
                .padding(vertical = 18.dp, horizontal = 20.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.icon_visual_cake),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "신짱구",
            style = NearTheme.typography.B2_14_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "25.03.20",
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
    }
}

@Preview
@Composable
fun MonthlyReminderCompletePreview() {
    NearTheme {
        MonthlyReminderComplete()
    }
}
