package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.ui.extension.ImageLoader
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ColumnScope.ContactLoadContent(
    contacts: List<FriendContactUIModel>,
    onDeselectContact: (String) -> Unit,
    onContactLoadClick: () -> Unit = {},
) {
    NearListModuleBackground(
        onClick = onContactLoadClick,
    ) {
        when (contacts.isEmpty()) {
            true -> {
                Image(
                    painter = painterResource(R.drawable.ic_front_24_gray),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(NearTheme.colors.GRAY01_888888),
                )
            }

            false -> {
                // 리스트가 있을 때 "다시 선택" 텍스트 표시
                Text(
                    text = stringResource(R.string.friend_contact_cycle_reselect_text),
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLUE01_5AA2E9,
                    textAlign = TextAlign.End,
                )
            }
        }
    }

    // 리스트가 있을 때만 밑에 리스트 표시
    if (contacts.isNotEmpty()) {
        Spacer(modifier = Modifier.size(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = contacts,
                key = { contact -> contact.id },
            ) { contact ->
                FriendListItem(
                    contact = contact,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_32_cancel),
                        contentDescription = stringResource(R.string.friend_contact_cycle_remove_friend_description),
                        colorFilter = ColorFilter.tint(NearTheme.colors.GRAY01_888888),
                        modifier =
                            Modifier
                                .size(24.dp)
                                .onNoRippleClick {
                                    onDeselectContact(contact.id.toString())
                                },
                    )
                }
            }
        }
    } else {
        // 리스트가 비어있을 때도 공간 확보
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun FriendListItem(
    modifier: Modifier = Modifier,
    contact: FriendContactUIModel,
    content: @Composable () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NearTheme.colors.BG02_F4F9FD)
                .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            ImageLoader(
                uri = contact.photoUri,
                modifier =
                    Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                placeholder = R.drawable.img_64_user_gray,
                error = R.drawable.img_64_user_gray,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = contact.name,
                style = NearTheme.typography.B2_14_MEDIUM,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
        }

        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ContactLoadContentPreview() {
    val contacts =
        listOf(
            FriendContactUIModel(
                id = 1,
                name = "신짱구",
                photoUri = null,
            ),
            FriendContactUIModel(
                id = 2,
                name = "철수",
                photoUri = null,
            ),
            FriendContactUIModel(
                id = 3,
                name = "유리",
                photoUri = null,
            ),
        )

    NearTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ContactLoadContent(
                contacts = contacts,
                onDeselectContact = {},
                onContactLoadClick = {},
            )
        }
    }
}
