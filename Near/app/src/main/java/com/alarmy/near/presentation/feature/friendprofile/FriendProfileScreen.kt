package com.alarmy.near.presentation.feature.friendprofile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.Friend
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendprofile.component.CallButton
import com.alarmy.near.presentation.feature.friendprofile.component.MessageButton
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendShipRecordState
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendState
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FriendProfileRoute(
    viewModel: FriendProfileViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit = {},
    onEditFriendInfo: (Friend) -> Unit = {},
    onClickCallButton: (phoneNumber: String) -> Unit = {},
    onClickMessageButton: (phoneNumber: String) -> Unit = {},
) {
    val friendState = viewModel.friendFlow.collectAsStateWithLifecycle()
    val friendShipRecordState = viewModel.friendShipRecordStateFlow.collectAsStateWithLifecycle()
    FriendProfileScreen(
        friendState = friendState.value,
        friendShipRecordState = friendShipRecordState.value,
        onClickBackButton = onClickBackButton,
        onEditFriendInfo = onEditFriendInfo,
        onClickCallButton = onClickCallButton,
        onClickMessageButton = onClickMessageButton,
    )
}

@Composable
fun FriendProfileScreen(
    modifier: Modifier = Modifier,
    friendState: FriendState,
    friendShipRecordState: FriendShipRecordState,
    onClickBackButton: () -> Unit = {},
    onEditFriendInfo: (Friend) -> Unit = {},
    onClickCallButton: (phoneNumber: String) -> Unit = {},
    onClickMessageButton: (phoneNumber: String) -> Unit = {},
) {
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val currentTabPosition = remember { mutableIntStateOf(0) }
    val dropdownState = remember { mutableStateOf(false) }
    Box(
        modifier =
            modifier
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(top = statusBarHeightDp, bottom = 24.dp),
    ) {
        when (friendState) {
            is FriendState.Success -> {
                val friend = friendState.friend
                Column(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .fillMaxSize()
                            .background(NearTheme.colors.WHITE_FFFFFF),
                ) {
                    NearTopAppbar(
                        title = stringResource(R.string.friend_profile_title),
                        onClickBackButton = onClickBackButton,
                        menuButton = {
                            Column(modifier = Modifier.padding(end = 20.dp)) {
                                Image(
                                    modifier =
                                        Modifier
                                            .onNoRippleClick(onClick = {
                                                dropdownState.value = true
                                            }),
                                    painter = painterResource(R.drawable.ic_32_menu),
                                    contentDescription = stringResource(R.string.common_menu_button_description),
                                )
                                DropdownMenu(
                                    modifier = Modifier.background(color = NearTheme.colors.WHITE_FFFFFF),
                                    expanded = dropdownState.value,
                                    shape = RoundedCornerShape(12.dp),
                                    onDismissRequest = { dropdownState.value = false },
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            onEditFriendInfo(friend)
                                            dropdownState.value = false
                                        },
                                        text = {
                                            Text(
                                                "수정",
                                                style = NearTheme.typography.B2_14_MEDIUM,
                                                color = NearTheme.colors.BLACK_1A1A1A,
                                            )
                                        },
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            dropdownState.value = false
                                        },
                                        text = {
                                            Text(
                                                "삭제",
                                                style = NearTheme.typography.B2_14_MEDIUM,
                                                color = NearTheme.colors.BLACK_1A1A1A,
                                            )
                                        },
                                    )
                                }
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                            Modifier,
                        ) {
                            Image(
                                modifier = Modifier.align(Alignment.Center),
                                painter = painterResource(R.drawable.img_80_user1),
                                contentDescription = null,
                            )
                            Image(
                                modifier =
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 2.dp, y = (-2).dp),
                                painter = painterResource(R.drawable.ic_visual_24_emoji_0),
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                modifier = Modifier.widthIn(max = 145.dp),
                                text = friend.name,
                                style = NearTheme.typography.B1_16_BOLD,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (friend.lastContactAt != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text =
                                        stringResource(
                                            R.string.friend_profile_last_contact_date_format,
                                            friend.lastContactAt.lastContactFormat(),
                                        ),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLUE01_5AA2E9,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                    ) {
                        CallButton(
                            modifier = Modifier.weight(1f),
                            enabled = !friend.phone.isNullOrBlank(),
                            onClick = {
                                friend.phone?.let {
                                    onClickCallButton(friend.phone)
                                }
                            },
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        MessageButton(
                            Modifier.weight(1f),
                            enabled = !friend.phone.isNullOrBlank(),
                            onClick = {
                                friend.phone?.let {
                                    onClickMessageButton(friend.phone)
                                }
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TabRow(
                        modifier =
                            Modifier
                                .padding(horizontal = 25.dp)
                                .width(170.dp),
                        containerColor = NearTheme.colors.WHITE_FFFFFF,
                        selectedTabIndex = 0,
                        divider = {},
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier =
                                    Modifier
                                        .customTabIndicatorOffset(
                                            it[currentTabPosition.intValue],
                                            80.dp,
                                        ), // 넓이, 애니메이션 지정
                                // 모양 지정
                                height = 3.dp,
                                color = NearTheme.colors.BLUE01_5AA2E9,
                            )
                        },
                    ) {
                        Tab(
                            modifier =
                                Modifier
                                    .width(85.dp)
                                    .height(50.dp),
                            selected = true,
                            onClick = {
                                currentTabPosition.intValue = 0
                            },
                        ) {
                            if (currentTabPosition.intValue == 0) {
                                Text(
                                    text = stringResource(R.string.friend_profile_tab_text_profile),
                                    style = NearTheme.typography.B2_14_BOLD,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.friend_profile_tab_text_profile),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.GRAY02_B7B7B7,
                                )
                            }
                        }
                        Tab(
                            modifier =
                                Modifier
                                    .width(85.dp)
                                    .height(50.dp),
                            selected = true,
                            onClick = {
                                currentTabPosition.intValue = 1
                            },
                        ) {
                            if (currentTabPosition.intValue == 1) {
                                Text(
                                    text = stringResource(R.string.friend_profile_tab_text_record),
                                    style = NearTheme.typography.B2_14_BOLD,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.friend_profile_tab_text_record),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.GRAY02_B7B7B7,
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = NearTheme.colors.GRAY03_EBEBEB)
                    if (currentTabPosition.intValue == 0) {
                        ProfileTab(friend = friend)
                    } else {
                        RecordTab(friendShipRecordState = friendShipRecordState)
                    }
                }
                NearSolidTypeButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(vertical = 17.dp),
                    enabled = true,
                    onClick = {},
                    text = stringResource(R.string.friend_profile_record_button_text),
                )
            }

            is FriendState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is FriendState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "프로필 정보를 불러오는데 실패했습니다.",
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTab(
    modifier: Modifier = Modifier,
    friend: Friend,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        ProfileDetailInfo(
            category = stringResource(R.string.friend_profile_info_category_relation),
            content = stringResource(friend.relation.resId),
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetailInfo(
            category = stringResource(R.string.friend_profile_info_category_term_of_contact),
            content = stringResource(friend.contactFrequency.reminderInterval.labelRes),
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetailInfo(
            category = stringResource(R.string.friend_profile_info_category_birthday),
            content = friend.birthday?.replace("-", ".") ?: "-",
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetailInfo(
            category = stringResource(R.string.friend_profile_info_category_anniversary),
            content =
                friend.anniversaryList.joinToString(" ") {
                    "${it.title} (${it.date})"
                },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileMemoInfo(
            content = friend.memo,
        )
    }
}

@Composable
private fun RecordTab(
    modifier: Modifier = Modifier,
    friendShipRecordState: FriendShipRecordState,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "챙김 기록",
            style = NearTheme.typography.B2_14_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A,
        )
        if (friendShipRecordState.records.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp))
                Image(painterResource(R.drawable.img_100_character_empty), contentDescription = null)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "이번달은 챙길 사람이 없네요.",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
            }
        } else {
            Spacer(modifier = Modifier.height(13.dp))
            LazyVerticalGrid(
                GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 60.dp),
            ) {
                items(friendShipRecordState.records.size) {
                    RecordItem(friendRecord = friendShipRecordState.records[it], index = it)
                }
            }
        }
    }
}

@Composable
private fun RecordItem(
    modifier: Modifier = Modifier,
    index: Int,
    friendRecord: FriendRecord,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = RoundedCornerShape(44.dp),
            border =
                BorderStroke(
                    color = NearTheme.colors.GRAY03_EBEBEB,
                    width = 1.dp,
                ),
            color = NearTheme.colors.WHITE_FFFFFF,
        ) {
            Column(
                modifier =
                    Modifier.padding(
                        top = 16.dp,
                        bottom = 20.dp,
                        start = 17.dp,
                        end = 17.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_40_character),
                    contentDescription = null,
                )
                Text(
                    "${index + 1}번째 챙김",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLUE01_5AA2E9,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            friendRecord.createdAt,
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.GRAY01_888888,
        )
    }
}

@Composable
private fun ProfileDetailInfo(
    modifier: Modifier = Modifier,
    category: String,
    content: String,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = NearTheme.colors.GRAY03_EBEBEB,
            ),
        color = NearTheme.colors.WHITE_FFFFFF,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                category,
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.GRAY01_888888,
            )
            Text(
                content,
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
        }
    }
}

@Composable
private fun ProfileMemoInfo(
    modifier: Modifier = Modifier,
    content: String?,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = NearTheme.colors.GRAY03_EBEBEB,
            ),
        color = NearTheme.colors.WHITE_FFFFFF,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.friend_profile_info_category_memo),
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.GRAY01_888888,
            )
            if (content.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(start = 54.dp),
                    text =
                        stringResource(R.string.friend_profile_info_memo_default_text),
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY02_B7B7B7,
                    textAlign = TextAlign.End,
                )
            } else {
                Text(
                    modifier = Modifier.padding(start = 54.dp),
                    text = content,
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )
            }
        }
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp,
): Modifier =
    composed(
        inspectorInfo =
            debugInspectorInfo {
                name = "customTabIndicatorOffset"
                value = currentTabPosition
            },
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = tabWidth,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
            label = "",
        )
        val indicatorOffset by animateDpAsState(
            targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
            label = "",
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart) // indicator 표시 위치
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }

private fun String.lastContactFormat(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("M월 d일")

    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}

@Preview(showBackground = true)
@Composable
fun FriendProfileScreenPreview() {
    NearTheme {
        FriendProfileScreen(
            friendState =
                FriendState.Success(
                    Friend(
                        friendId = "adfaggasf",
                        imageUrl = "",
                        relation = Relation.FRIEND,
                        name = "",
                        contactFrequency =
                            ContactFrequency(
                                reminderInterval = ReminderInterval.EVERY_TWO_WEEK,
                                dayOfWeek = "MONDAY",
                            ),
                        birthday = "1998-11-13",
                        anniversaryList = listOf(),
                        memo = "",
                        phone = "",
                        lastContactAt = "",
                    ),
                ),
            friendShipRecordState =
                FriendShipRecordState(
                    records =
                        List(5) {
                            FriendRecord(
                                isChecked = true,
                                createdAt = "2023-11-1$it",
                            )
                        },
                ),
        )
    }
}
