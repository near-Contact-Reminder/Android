package com.alarmy.near.presentation.feature.friend

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun FriendProfileRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
}

@Composable
fun FriendProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun FriendProfileScreenPreview() {
    NearTheme {
        FriendProfileScreen()
    }
}
