package com.alarmy.near.presentation.feature.mothlyreminderall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.extension.dropShadow
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun MonthlyReminderFriendCard(
    reminder: MonthlyReminderUIModel,
    onRecordClick: (String) -> Unit = {},
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .dropShadow(
                    shape = RoundedCornerShape(12.dp),
                    blur = 16.dp,
                    spread = 0.dp,
                    offsetX = 0.dp,
                    offsetY = 4.dp,
                    color = Color.Black.copy(0.12f),
                ),
        colors =
            CardDefaults.cardColors(
                containerColor = NearTheme.colors.WHITE_FFFFFF,
            ),
    ) {
        Spacer(modifier = Modifier.size(20.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = reminder.imageRes),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = reminder.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = NearTheme.typography.B2_14_BOLD,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = reminder.description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            val (textStyle, textColor) =
                if (reminder.daysUntilNextContact == "D-DAY") {
                    NearTheme.typography.B2_14_BOLD to NearTheme.colors.BLUE01_5AA2E9
                } else {
                    NearTheme.typography.B2_14_MEDIUM to NearTheme.colors.GRAY01_888888
                }

            Text(
                modifier = Modifier.align(Alignment.Top),
                text = reminder.daysUntilNextContact,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = textStyle,
                color = textColor,
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        NearBasicButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            onClick = { onRecordClick(reminder.friendId) },
            contentPadding = PaddingValues(12.dp),
        ) {
            Text(
                "챙김 기록하기",
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun MonthlyReminderFriendCardPreview() {
    NearTheme {
        MonthlyReminderFriendCard(
            reminder =
                MonthlyReminderUIModel(
                    friendId = "1",
                    name = "신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구",
                    imageRes = R.drawable.icon_visual_cake,
                    description = "생일 축하 전해요",
                    nextContactAt = "2025-11-05",
                    daysUntilNextContact = "D-DAY",
                ),
        )
    }
}
