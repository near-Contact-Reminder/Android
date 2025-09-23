package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ColumnScope.ContactCycleContent(contacts: List<FriendContactUIModel>) {
    var isBulkSettingEnabled by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "한번에 설정",
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
        Spacer(modifier = Modifier.size(12.dp))

        NearBackgroundCheckbox(
            checked = isBulkSettingEnabled,
            onCheckedChange = { checked ->
                isBulkSettingEnabled = checked
                // 체크버튼이 활성화되면 바텀시트 표시
                if (checked) {
                    isBottomSheetVisible = true
                }
            },
        )
    }

    Spacer(modifier = Modifier.size(14.dp))

    // 한번에 설정이 활성화되었을 때만 표시
    if (isBulkSettingEnabled) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, NearTheme.colors.GRAY03_EBEBEB),
                        shape = RoundedCornerShape(12.dp),
                    ).padding(
                        horizontal = 16.dp,
                        vertical = 14.dp,
                    ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "매주 화요일",
            )

            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_24_down),
                contentDescription = null,
                colorFilter = ColorFilter.tint(NearTheme.colors.GRAY01_888888),
            )
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
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "주기 설정",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )

                        Spacer(modifier = Modifier.size(2.dp))

                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_24_down),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(NearTheme.colors.GRAY01_888888),
                        )
                    }
                }
            }
        }
    } else {
        // 리스트가 비어있을 때도 공간 확보
        Spacer(modifier = Modifier.weight(1f))
    }

    // 바텀시트 표시
    CycleSettingBottomSheet(
        isVisible = isBottomSheetVisible,
        onDismiss = {
            isBottomSheetVisible = false
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ContactCycleContentPreview() {
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
            ContactCycleContent(
                contacts = contacts,
            )
        }
    }
}
