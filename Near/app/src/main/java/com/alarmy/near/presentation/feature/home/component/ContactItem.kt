package com.alarmy.near.presentation.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.ContactSummary
import com.alarmy.near.presentation.ui.theme.NearTheme
import java.time.LocalDate

private const val MAX_WIDTH_OF_NAME_TEXT = 97

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    contactSummary: ContactSummary,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Image(
                painter = painterResource(R.drawable.img_64_user1),
                contentDescription = "",
            )
            Image(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp),
                painter =
                    when (contactSummary.contactFrequency) {
                        ContactFrequency.LOW -> painterResource(R.drawable.ic_visual_24_emoji_0)
                        ContactFrequency.MIDDLE -> painterResource(R.drawable.ic_visual_24_emoji_50)
                        ContactFrequency.HIGH -> painterResource(R.drawable.ic_visual_24_emoji_100)
                    },
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(MAX_WIDTH_OF_NAME_TEXT.dp),
            text = contactSummary.name,
            style = NearTheme.typography.B2_14_BOLD,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = NearTheme.colors.BLACK_1A1A1A,
        )
        Spacer(modifier = Modifier.height(1.dp))
        Row {
            Text(
                contactSummary.formattedDate,
                style = NearTheme.typography.FC_12_MEDIUM,
                textAlign = TextAlign.Center,
                color = NearTheme.colors.GRAY02_B7B7B7,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Image(
                painter = painterResource(R.drawable.ic_12_check),
                contentDescription = "",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview_Default() {
    ContactItem(
        modifier = Modifier.padding(top = 10.dp),
        contactSummary =
            ContactSummary(
                id = 1L,
                name = "홍길동",
                profileImageUrl = "",
                lastContactedAt = LocalDate.of(2025, 5, 31),
                isContacted = true,
                contactFrequency = ContactFrequency.HIGH,
        )
    )
}
