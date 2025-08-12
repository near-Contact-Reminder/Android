package com.alarmy.near.presentation.feature.friend

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.friend.component.CallButton
import com.alarmy.near.presentation.feature.friend.component.MessageButton
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun FriendProfileRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
}

@Composable
fun FriendProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF),
    ) {
        NearTopAppbar(
            title = "프로필 상세",
            isMenuVisible = true,
            onClickBackButton = {},
            onClickMenuButton = {},
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
                    painter = painterResource(R.drawable.ic_visual_24_emoji_100),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(
                    modifier = Modifier.widthIn(max = 145.dp),
                    text = "일이삼사오육칠",
                    style = NearTheme.typography.B1_16_BOLD,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "3월 22일 더 가까워졌어요",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLUE01_5AA2E9,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
        ) {
            CallButton(modifier = Modifier.weight(1f), onClick = {})
            Spacer(modifier = Modifier.width(7.dp))
            MessageButton(Modifier.weight(1f), onClick = {})
        }
        Spacer(modifier = Modifier.height(24.dp))
        val currentTabPosition = remember { mutableIntStateOf(0) }
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
                onClick = {},
            ) {
                Text(
                    text = "프로필",
                    style = NearTheme.typography.B2_14_BOLD,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )
            }
            Tab(
                modifier =
                    Modifier
                        .width(85.dp)
                        .height(50.dp),
                selected = true,
                onClick = {},
            ) {
                Text(
                    text = "기록",
                    style = NearTheme.typography.B2_14_BOLD,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = NearTheme.colors.GRAY03_EBEBEB)


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
        FriendProfileScreen()
    }
}
