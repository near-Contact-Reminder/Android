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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel
import com.alarmy.near.presentation.ui.theme.NearTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MonthlyReminderComplete(reminder: MonthlyReminderUIModel) {
    val formattedDate = formatDate(reminder.nextContactAt)

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
            painter = painterResource(reminder.imageRes),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = reminder.name,
            style = NearTheme.typography.B2_14_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = formattedDate,
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
    }
}

private fun formatDate(dateString: String): String =
    try {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        date.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
    } catch (e: Exception) {
        dateString
    }

@Preview
@Composable
fun MonthlyReminderCompletePreview() {
    NearTheme {
        MonthlyReminderComplete(
            reminder =
                MonthlyReminderUIModel(
                    friendId = "1",
                    name = "신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구신짱구",
                    imageRes = R.drawable.icon_visual_cake,
                    descriptionRes = R.string.monthly_reminder_all_type_birthday_description,
                    nextContactAt = "2025-03-20",
                    daysUntilNextContact = "D-9",
                    isToday = false,
                ),
        )
    }
}
