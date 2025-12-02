package com.alarmy.near.presentation.feature.chatbot.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotAppbar() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .border(
                        shape = RoundedCornerShape(8.dp),
                        border =
                            BorderStroke(
                                1.dp,
                                NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.1f),
                            ),
                    ).onNoRippleClick {}
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp, bottom = 7.dp),
            text = "추천 메시지 기록",
            style = NearTheme.typography.B2_14_MEDIUM,
        )

        Spacer(modifier = Modifier.size(12.dp))

        IconButton(
            onClick = { },
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
fun ChatbotAppbarPreview() {
    NearTheme {
        ChatbotAppbar()
    }
}
