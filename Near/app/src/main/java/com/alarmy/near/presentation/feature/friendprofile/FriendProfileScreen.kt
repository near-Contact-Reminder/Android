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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.DayOfWeek
import com.alarmy.near.model.Friend
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendprofile.component.CallButton
import com.alarmy.near.presentation.feature.friendprofile.component.MessageButton
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendProfileUIEvent
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendShipRecordState
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.component.dropdown.NearDropdownMenu
import com.alarmy.near.presentation.ui.component.dropdown.NearDropdownMenuItem
import com.alarmy.near.presentation.ui.extension.ImageLoader
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FriendProfileRoute(
    viewModel: FriendProfileViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit = {},
    onEditFriendInfo: (Friend) -> Unit = {},
    onClickCallButton: (phoneNumber: String) -> Unit = {},
    onClickMessageButton: (phoneNumber: String) -> Unit = {},
    onDeleteFriendSuccess: (friendId: String) -> Unit = {},
) {
    val friendState = viewModel.friendFlow.collectAsStateWithLifecycle()
    val friendShipRecordState = viewModel.friendShipRecordStateFlow.collectAsStateWithLifecycle()
    val recordSuccessDialogState = remember { mutableStateOf(false) }
    LaunchedEffect(viewModel.uiEvent) {
        launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is FriendProfileUIEvent.NetworkError -> {
                        onShowErrorSnackBar(IllegalStateException("네트워크 에러가 발생했습니다."))
                    }

                    is FriendProfileUIEvent.DeleteFriendSuccess -> {
                        onDeleteFriendSuccess(event.friendId)
                    }

                    is FriendProfileUIEvent.RecordFriendShipSuccess -> {
                        recordSuccessDialogState.value = true
                    }
                }
            }
        }
    }
    FriendProfileScreen(
        friendState = friendState.value,
        friendShipRecordState = friendShipRecordState.value,
        recordSuccessDialogState = recordSuccessDialogState.value,
        onClickBackButton = onClickBackButton,
        onEditFriendInfo = onEditFriendInfo,
        onClickCallButton = onClickCallButton,
        onClickMessageButton = onClickMessageButton,
        onRecordFriendShip = viewModel::onRecordFriendShip,
        onDeleteFriend = viewModel::onDeleteFriend,
        onDismissRecordSuccessDialog = {
            recordSuccessDialogState.value = false
        },
    )
}

@Composable
fun FriendProfileScreen(
    modifier: Modifier = Modifier,
    friendState: FriendState,
    friendShipRecordState: FriendShipRecordState,
    recordSuccessDialogState: Boolean = false,
    onClickBackButton: () -> Unit = {},
    onEditFriendInfo: (Friend) -> Unit = {},
    onClickCallButton: (phoneNumber: String) -> Unit = {},
    onClickMessageButton: (phoneNumber: String) -> Unit = {},
    onRecordFriendShip: (friendId: String) -> Unit = {},
    onDeleteFriend: (friendId: String) -> Unit = {},
    onDismissRecordSuccessDialog: () -> Unit = {},
) {
    val currentTabPosition = remember { mutableIntStateOf(0) }
    val dropdownState = remember { mutableStateOf(false) }

    NearFrame(modifier = modifier) {
        if (recordSuccessDialogState) {
            LaunchedEffect(true) {
                if (recordSuccessDialogState) {
                    delay(2000L)
                    onDismissRecordSuccessDialog()
                }
            }
            Dialog(onDismissRequest = onDismissRecordSuccessDialog) {
                Column(
                    modifier =
                        Modifier
                            .width(255.dp)
                            .height(186.dp)
                            .background(
                                color = NearTheme.colors.WHITE_FFFFFF,
                                shape = RoundedCornerShape(16.dp),
                            ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painterResource(R.drawable.img_100_character_success),
                        contentDescription = "",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.friend_profile_info_contact_success_text),
                        style = NearTheme.typography.B1_16_BOLD,
                        color = Color(0xff222222),
                    )
                }
            }
        }
        Box {
            when (friendState) {
                is FriendState.Success -> {
                    val friend = friendState.friend

                    // 전체를 세로로, 앱바는 고정, 나머지는 weight(1f) 로 영역 제한
                    Column(
                        modifier =
                            Modifier
                                .align(Alignment.TopStart)
                                .fillMaxSize()
                                .background(NearTheme.colors.WHITE_FFFFFF),
                    ) {
                        // (1) 상단 AppBar — 고정
                        NearTopAppbar(
                            title = stringResource(R.string.friend_profile_title),
                            onClickBackButton = onClickBackButton,
                            menuButton = {
                                Column(modifier = Modifier.padding(end = 20.dp)) {
                                    Image(
                                        modifier =
                                            Modifier.onNoRippleClick(onClick = {
                                                dropdownState.value = true
                                            }),
                                        painter = painterResource(R.drawable.ic_32_menu),
                                        contentDescription = stringResource(R.string.common_menu_button_description),
                                    )
                                    NearDropdownMenu(
                                        expanded = dropdownState.value,
                                        onDismissRequest = { dropdownState.value = false },
                                    ) {
                                        NearDropdownMenuItem(
                                            onClick = {
                                                onEditFriendInfo(friend)
                                                dropdownState.value = false
                                            },
                                            text = stringResource(R.string.friend_profile_info_edit),
                                        )
                                        NearDropdownMenuItem(
                                            onClick = {
                                                onDeleteFriend(friend.friendId)
                                                dropdownState.value = false
                                            },
                                            text = stringResource(R.string.friend_profile_info_delete),
                                        )
                                    }
                                }
                            },
                        )

                        // (2) 나머지 영역을 제한된 높이로 만들기 -> 이 안에서 스크롤 가능
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f), // <--- 요게 중요: 여기서 높이가 제한되어 내부 스크롤이 가능해짐
                        ) {
                            // 컨텐츠는 스크롤 가능하거나 Lazy로 구성
                            // 여기서는 상단의 프로필 요약(이미지/이름/버튼 등)을 스크롤 헤더로 포함시키고
                            // 탭에 따라 ProfileTab(스크롤 필요 없음) 또는 RecordTab(LazyVerticalGrid)를 표시
                            // 상단 요약을 포함한 전체를 LazyColumn으로 만들면 header + list 형태로 자연스럽게 동작

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                item {
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
                                                Modifier
                                                    .size(80.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            ImageLoader(
                                                uri = friend.imageUrl,
                                                modifier =
                                                    Modifier
                                                        .matchParentSize()
                                                        .clip(CircleShape),
                                                placeholder = R.drawable.img_80_user1,
                                                error = R.drawable.img_80_user1,
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
                                                            friend.lastContactFormat ?: "",
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
                                    // TabRow
                                    TabRow(
                                        modifier =
                                            Modifier
                                                .padding(horizontal = 25.dp)
                                                .width(170.dp),
                                        containerColor = NearTheme.colors.WHITE_FFFFFF,
                                        selectedTabIndex = currentTabPosition.intValue,
                                        divider = {},
                                        indicator = {
                                            TabRowDefaults.SecondaryIndicator(
                                                modifier =
                                                    Modifier.customTabIndicatorOffset(
                                                        it[currentTabPosition.intValue],
                                                        80.dp,
                                                    ),
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
                                            selectedContentColor =
                                                NearTheme.colors.GRAY01_888888.copy(
                                                    alpha = 0.3f,
                                                ),
                                            selected = currentTabPosition.intValue == 0,
                                            onClick = { currentTabPosition.intValue = 0 },
                                        ) {
                                            Text(
                                                text = stringResource(R.string.friend_profile_tab_text_profile),
                                                style =
                                                    if (currentTabPosition.intValue ==
                                                        0
                                                    ) {
                                                        NearTheme.typography.B2_14_BOLD
                                                    } else {
                                                        NearTheme.typography.B2_14_MEDIUM
                                                    },
                                                color =
                                                    if (currentTabPosition.intValue ==
                                                        0
                                                    ) {
                                                        NearTheme.colors.BLACK_1A1A1A
                                                    } else {
                                                        NearTheme.colors.GRAY02_B7B7B7
                                                    },
                                            )
                                        }
                                        Tab(
                                            modifier =
                                                Modifier
                                                    .width(85.dp)
                                                    .height(50.dp),
                                            selected = currentTabPosition.intValue == 1,
                                            onClick = { currentTabPosition.intValue = 1 },
                                            selectedContentColor =
                                                NearTheme.colors.GRAY01_888888.copy(
                                                    alpha = 0.3f,
                                                ),
                                        ) {
                                            Text(
                                                text = stringResource(R.string.friend_profile_tab_text_record),
                                                style =
                                                    if (currentTabPosition.intValue ==
                                                        1
                                                    ) {
                                                        NearTheme.typography.B2_14_BOLD
                                                    } else {
                                                        NearTheme.typography.B2_14_MEDIUM
                                                    },
                                                color =
                                                    if (currentTabPosition.intValue ==
                                                        1
                                                    ) {
                                                        NearTheme.colors.BLACK_1A1A1A
                                                    } else {
                                                        NearTheme.colors.GRAY02_B7B7B7
                                                    },
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = NearTheme.colors.GRAY03_EBEBEB,
                                    )
                                } // end header item

                                // 탭별 본문을 lazy column의 item으로 넣음 — 이렇게 하면 중첩 스크롤 문제 회피
                                item {
                                    if (currentTabPosition.intValue == 0) {
                                        // ProfileTab : 내부에 스크롤을 또 만들 필요 없음. LazyColumn이 전체를 스크롤함.
                                        ProfileTab(friend = friend)
                                    } else {
                                        RecordTab(
                                            friendShipRecordState = friendShipRecordState,
                                        )
                                    }
                                }
                                item { Spacer(modifier = Modifier.height(80.dp)) } // 하단 여유
                            } // end LazyColumn
                        } // end weighted Column

                        // (3) 하단 고정 버튼
                        Box(
                            modifier =
                                Modifier.fillMaxWidth().background(
                                    brush =
                                        Brush.linearGradient(
                                            colors = listOf(Color(0x00FFFFFF), Color(0xFFFFFFFF)), // 파랑 → 밝은 하늘색
                                            start = Offset(0f, 0f), // 위쪽 시작
                                            end = Offset(0f, Float.POSITIVE_INFINITY),
                                        ),
                                ),
                        ) {
                            NearSolidTypeButton(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(bottom = 24.dp, top = 16.dp),
                                contentPadding = PaddingValues(vertical = 17.dp),
                                enabled = friend.isContactToday?.not() ?: false,
                                onClick = { onRecordFriendShip(friend.friendId) },
                                text = stringResource(R.string.friend_profile_record_button_text),
                            )
                        }
                    } // end parent Column
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
            } // end when
        } // end Box
    } // end NearFrame
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
                    "${it.title} (${it.date?.replace("-",".")})"
                },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileMemoInfo(
            content = friend.memo,
        )
        Spacer(modifier = Modifier.height(76.dp))
    }
}

@Composable
private fun RecordTab(
    modifier: Modifier = Modifier,
    friendShipRecordState: FriendShipRecordState,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth() // 부모 스크롤(LazyColumn)에 의존하도록 fillMaxWidth만 사용
                .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            stringResource(R.string.friend_profile_info_record_title_text),
            style = NearTheme.typography.B2_14_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A,
        )

        if (friendShipRecordState.isEmpty) {
            // 빈 상태는 중앙 정렬이지만 높이를 무한으로 잡지 않도록 fillMaxWidth 사용
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(R.drawable.img_100_character_empty), contentDescription = null)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.friend_profile_info_empty_contact_friend),
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
            }
        } else {
            Spacer(modifier = Modifier.height(13.dp))

            // records를 3개씩 묶어서 행(Row)으로 렌더 — 자체 스크롤 없음
            val rows = friendShipRecordState.records.chunked(3)
            Column(modifier = Modifier.fillMaxWidth()) {
                rows.forEach { rowItems ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                        // 아이템 간 세로 간격
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        // 각 row의 아이템을 동일한 폭으로 나눔
                        rowItems.forEachIndexed { index, item ->
                            // 인덱스나 번호 표시 로직은 기존과 동일하게 계산
                            val globalIndex =
                                friendShipRecordState.records.size - (rows.indexOf(rowItems) * 3 + index)
                            Box(modifier = Modifier.weight(1f)) {
                                RecordItem(
                                    modifier = Modifier.wrapContentSize(),
                                    index = globalIndex,
                                    friendRecord = item,
                                )
                            }
                        }

                        // 만약 마지막 행에 3 미만의 아이템이 있으면 빈 공간을 채워 균형 맞춤
                        if (rowItems.size < 3) {
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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
                    text = stringResource(R.string.friend_profile_info_contact_record_text, index),
                    textAlign = TextAlign.Center,
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
                                dayOfWeek = DayOfWeek.THURSDAY,
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
