package com.alarmy.near.presentation.feature.chatbot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotSentBubble(text: String) {
    Box(
        modifier =
            Modifier
                .background(NearTheme.colors.BG01_E3F0F9, RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Text(
            text = text,
            style = NearTheme.typography.B2_14_MEDIUM,
        )
    }
}

@Preview
@Composable
fun ChatbotItemPreview() {
    NearTheme {
        ChatbotSentBubble(
            text =
                "마지막으로 '뉴질랜드 여행' 갔다온 안부를\n" +
                    "물었는데 뭐라고 다시 연락하면 좋을까?\n" +
                    "장난스러운 말투로 제안해줬으면 좋겠어\n" +
                    "마지막으로 '뉴질랜드 여행' 갔다온 안부",
        )
    }
}
