package com.alarmy.near.presentation.feature.chatbot.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotReceivedBubble(
    text: String,
) {
    Column {
        Image(
            painter = painterResource(R.drawable.img_100_character_default),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = text,
            style = NearTheme.typography.B2_14_MEDIUM,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Row {
            Icon(
                painter = painterResource(R.drawable.icon_24_copy),
                tint = NearTheme.colors.GRAY01_888888,
                contentDescription = "복사",
                modifier = Modifier.clickable { /* 복사 로직 */ },
            )
            Spacer(modifier = Modifier.size(12.dp))
            Icon(
                painter = painterResource(R.drawable.icon_24_share),
                tint = NearTheme.colors.GRAY01_888888,
                contentDescription = "공유",
                modifier = Modifier.clickable { /* 공유 로직 */ },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotReceivedBubblePreview() {
    NearTheme {
        ChatbotReceivedBubble(
            text = "\"지난번에 뉴질랜드 여행 다녀왔다고 했었죠? 잘 다녀왔나요? 멋진 추억 많이 만들었길 바라요!\"",
        )
    }
}
