package com.alarmy.near.presentation.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.friendsummary.ContactFrequencyLevel
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.model.monthly.MonthlyFriendType
import com.alarmy.near.presentation.feature.home.component.MyContacts
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import com.alarmy.near.presentation.feature.home.model.MonthlyFriendUIState
import com.alarmy.near.presentation.feature.home.model.MyFriendUIState
import com.alarmy.near.presentation.ui.component.dropdown.NearDropdownMenu
import com.alarmy.near.presentation.ui.component.dropdown.NearDropdownMenuItem
import com.alarmy.near.presentation.ui.extension.NearConditionalShimmer
import com.alarmy.near.presentation.ui.extension.ShimmerType
import com.alarmy.near.presentation.ui.extension.dropShadow
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import java.time.LocalDate

private const val MINIMUM_PAGE_COUNT_TO_SHOW_UI = 2

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onContactClick: (String) -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onMyPageClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},
    onMonthlyReminderAllClick: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect {
            onShowErrorSnackBar(IllegalStateException("네트워크 에러가 발생했습니다."))
        }
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState.value,
        onContactClick = onContactClick,
        onAlarmClick = onAlarmClick,
        onMyPageClick = onMyPageClick,
        onAddContactClick = onAddContactClick,
        onMonthlyReminderAllClick = onMonthlyReminderAllClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onContactClick: (String) -> Unit = { _ -> },
    onMyPageClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},
    onMonthlyReminderAllClick: () -> Unit = {},
    uiState: HomeUiState,
) {
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }

    Surface(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .paint(
                        painter =
                            painterResource(
                                R.drawable.img_bg,
                            ),
                        contentScale = ContentScale.FillBounds,
                    )
                    .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(statusBarHeightDp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.onNoRippleClick(onClick = onMyPageClick),
                    text = stringResource(R.string.home_my_profile_button_text),
                    style = NearTheme.typography.H2_18_BOLD.copy(letterSpacing = 0.sp),
                    color = NearTheme.colors.WHITE_FFFFFF,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    modifier = Modifier.onNoRippleClick(onClick = onAlarmClick),
                    painter = painterResource(R.drawable.ic_32_bell),
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                NearConditionalShimmer(enabled = uiState.memberInfo == null) {
                    Text(
                        text =
                            buildAnnotatedString {
                                append("${uiState.memberInfo?.nickname}님,\n")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("누구를 챙길지")
                                }
                                append(" 정해볼까요?")
                            },
                        style = NearTheme.typography.H1_24_REGULAR,
                        color = NearTheme.colors.WHITE_FFFFFF,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.home_this_month_people),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = NearTheme.typography.B1_16_BOLD,
                    color = NearTheme.colors.WHITE_FFFFFF,
                )

                MonthlyReminderFriendsViewAll(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onMonthlyReminderAllClick = onMonthlyReminderAllClick,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.monthlyFriendUIState is MonthlyFriendUIState.Loading) {
                NearConditionalShimmer(
                    enabled = true,
                    modifier =
                        Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                ) {}
            } else if (uiState.monthlyFriendUIState is MonthlyFriendUIState.Success) {
                if (uiState.monthlyFriendUIState.monthlyFriends.isEmpty()) {
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        color = NearTheme.colors.WHITE_FFFFFF.copy(alpha = 0.2f),
                    ) {
                        Text(
                            text = stringResource(R.string.home_no_people_this_month),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                            textAlign = TextAlign.Center,
                            style =
                                NearTheme.typography.B2_14_MEDIUM.copy(
                                    fontWeight = FontWeight.Normal,
                                ),
                            color = NearTheme.colors.WHITE_FFFFFF,
                        )
                    }
                } else {
                    val monthlyFriends = uiState.monthlyFriendUIState.monthlyFriends
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            count = monthlyFriends.size,
                            key = {
                                monthlyFriends[it].friendId
                            },
                        ) {
                            val monthlyContact = monthlyFriends[it]
                            val now = LocalDate.now()
                            Surface(
                                modifier.dropShadow(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.Black.copy(alpha = 0.07f),
                                    blur = 4.dp,
                                    offsetY = 4.dp,
                                ),
                                color = NearTheme.colors.WHITE_FFFFFF,
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Row(
                                    modifier =
                                        Modifier
                                            .padding(start = 12.dp, end = 16.dp)
                                            .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painterResource(monthlyContact.type.imageSrc),
                                        contentDescription = "",
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        modifier = Modifier.widthIn(max = 97.dp),
                                        text = monthlyContact.name,
                                        style = NearTheme.typography.B2_14_BOLD,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        color = NearTheme.colors.BLACK_1A1A1A,
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    if (monthlyContact.isNextContactDay(now)) {
                                        Text(
                                            text = monthlyContact.daysUntilNextContact(LocalDate.now()),
                                            style = NearTheme.typography.B2_14_BOLD,
                                            color = NearTheme.colors.BLUE01_5AA2E9,
                                        )
                                    } else {
                                        Text(
                                            text = monthlyContact.daysUntilNextContact(now),
                                            style = NearTheme.typography.B2_14_MEDIUM,
                                            color = NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.5f),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            MyFriends(
                onContactClick = onContactClick,
                onAddContactClick = onAddContactClick,
                myFriendUIState = uiState.myFriendUIState,
            )
        }
    }
}

@Composable
private fun MyFriends(
    myFriendUIState: MyFriendUIState,
    onContactClick: (String) -> Unit,
    onAddContactClick: () -> Unit,
) {
    val contactsWithPage =
        if (myFriendUIState is MyFriendUIState.Success) myFriendUIState.myFriends.chunked(5) else listOf()
    val pagerState: PagerState =
        rememberPagerState(
            initialPage = 0,
            pageCount = {
                contactsWithPage.count() +
                    if (contactsWithPage
                            .lastOrNull()
                            ?.count() == 5
                    ) {
                        1
                    } else {
                        0
                    }
            },
        )
    val dropdownState = remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    color = NearTheme.colors.WHITE_FFFFFF,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                ),
    ) {
        if (myFriendUIState is MyFriendUIState.Loading) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(166.dp))
                NearConditionalShimmer(
                    shimmerColors = ShimmerType.WHITE.colors,
                    modifier = Modifier.size(width = 151.dp, height = 125.dp),
                    enabled = true,
                ) {
                }
            }
        } else {
            MyContacts(
                modifier = Modifier.align(Alignment.TopCenter),
                contactsWithPage = contactsWithPage,
                pagerState = pagerState,
                onContactClick = onContactClick,
                onAddContactClick = {
                    onAddContactClick()
                },
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 24.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(R.string.home_my_people),
                style = NearTheme.typography.H2_18_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
            Column {
                Image(
                    modifier =
                        Modifier.onNoRippleClick(onClick = {
                            dropdownState.value = true
                        }),
                    painter = painterResource(R.drawable.ic_32_menu),
                    contentDescription = stringResource(R.string.home_my_people_setting),
                )
                NearDropdownMenu(
                    expanded = dropdownState.value,
                    onDismissRequest = { dropdownState.value = false },
                ) {
                    NearDropdownMenuItem(
                        onClick = {
                            onAddContactClick()
                            dropdownState.value = false
                        },
                        text = stringResource(R.string.home_menu_text_add_friend),
                    )
                }
            }
        }

        if (contactsWithPage.size >= MINIMUM_PAGE_COUNT_TO_SHOW_UI) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                PagerIndicator(pagerState)
                Spacer(modifier = Modifier.height(104.dp))
            }
        }
    }
}

@Composable
private fun PagerIndicator(pagerState: PagerState) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) {
                    Color(0xff737373)
                } else {
                    Color(
                        0xffe2e2e2,
                    )
                }
            Box(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp),
            )
        }
    }
}

@Composable
fun MonthlyReminderFriendsViewAll(
    modifier: Modifier = Modifier,
    onMonthlyReminderAllClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier.onNoRippleClick(
                onClick = onMonthlyReminderAllClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.home_monthly_friends_all),
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.WHITE_FFFFFF,
            modifier = Modifier.alpha(0.8f),
        )

        Spacer(modifier = Modifier.size(6.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_front_8),
            colorFilter = ColorFilter.tint(NearTheme.colors.WHITE_FFFFFF),
            alpha = 1f,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
internal fun HomeScreenPreview() {
    NearTheme {
        HomeScreen(
            onContactClick = {},
            uiState =
                HomeUiState(
                    myFriendUIState =
                        MyFriendUIState.Loading,
                    monthlyFriendUIState =
                        MonthlyFriendUIState.Success(List(4) {
                            MonthlyFriend(
                                friendId = "intellegat$it",
                                name = "Stacey Stewart",
                                type = MonthlyFriendType.ANNIVERSARY,
                                nextContactAt = "2025-09-30",
                            )
                        },),
                    memberInfo =
                    null,
                ),
        )
    }
}
