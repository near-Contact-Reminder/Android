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
import com.alarmy.near.model.friendsummary.ContactFrequencyLevel
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

private const val MAX_WIDTH_OF_NAME_TEXT = 97

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    friendSummary: FriendSummary,
    onClick: (contactId: String) -> Unit = {},
) {
    Column(
        modifier =
            modifier.onNoRippleClick {
                onClick(friendSummary.id)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                    when (friendSummary.contactFrequencyLevel) {
                        ContactFrequencyLevel.LOW -> painterResource(R.drawable.ic_visual_24_emoji_0)
                        ContactFrequencyLevel.MIDDLE -> painterResource(R.drawable.ic_visual_24_emoji_50)
                        ContactFrequencyLevel.HIGH -> painterResource(R.drawable.ic_visual_24_emoji_100)
                    },
                contentDescription = "",
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(MAX_WIDTH_OF_NAME_TEXT.dp),
            text = friendSummary.name,
            style = NearTheme.typography.B2_14_BOLD,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = NearTheme.colors.BLACK_1A1A1A,
        )
        Spacer(modifier = Modifier.height(1.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                friendSummary.lastContactedAt ?: "",
                style = NearTheme.typography.FC_12_MEDIUM,
                textAlign = TextAlign.Center,
                color = NearTheme.colors.GRAY02_B7B7B7,
            )
            Spacer(modifier = Modifier.width(2.dp))
            if (friendSummary.lastContactedAt != null) {
                Image(
                    painter = painterResource(R.drawable.ic_12_check),
                    contentDescription = "",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactItem(
        modifier = Modifier.padding(top = 10.dp),
        friendSummary =
            FriendSummary(
                id = "123L",
                name = "홍길동",
                profileImageUrl = "",
                lastContactedAt = "2025-04-21",
                isContacted = true,
                contactFrequencyLevel = ContactFrequencyLevel.HIGH,
            ),
    )
}
