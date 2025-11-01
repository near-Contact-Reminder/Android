package com.alarmy.near.presentation.feature.mothlyreminderall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun MonthlyReminderEmpty() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.28f))

        Image(
            painter = painterResource(R.drawable.img_100_character_empty),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = stringResource(R.string.monthly_reminder_all_empty_text),
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyReminderEmptyPreview() {
    NearTheme {
        MonthlyReminderEmpty()
    }
}
