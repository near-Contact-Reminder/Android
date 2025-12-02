package com.alarmy.near.presentation.feature.chatbotrecord.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun ChatbotRecordItem(
    record: ChatbotRecordUIModel,
    onItemClick: (ChatbotRecordUIModel) -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = NearTheme.colors.BG02_F4F9FD,
                    shape = RoundedCornerShape(12.dp),
                ).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = record.title,
            style = NearTheme.typography.B2_14_BOLD,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = record.date,
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
    }
}

@Preview
@Composable
fun ChatbotRecordItemPreview() {
    NearTheme {
        ChatbotRecordItem(
            record =
                ChatbotRecordUIModel(
                    id = "1",
                    title = "야구에 관한 이야기",
                    date = "2023-06-01",
                ),
        )
    }
}
