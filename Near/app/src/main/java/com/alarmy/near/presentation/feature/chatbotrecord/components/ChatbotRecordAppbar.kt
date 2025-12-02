package com.alarmy.near.presentation.feature.chatbotrecord.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotRecordAppbar(
    onNavigateBack: () -> Unit = {},
    onRecordClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
                Modifier
                    .align(Alignment.CenterVertically),
            text = "추천 메시지 기록",
            style = NearTheme.typography.B1_16_BOLD,
        )

        IconButton(
            onClick = { onNavigateBack() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_32_cancel),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotRecordAppbarPreview() {
    NearTheme {
        ChatbotRecordAppbar()
    }
}
