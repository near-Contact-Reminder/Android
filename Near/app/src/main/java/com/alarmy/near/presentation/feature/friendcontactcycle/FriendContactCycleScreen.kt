package com.alarmy.near.presentation.feature.friendcontactcycle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactLoadContent
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun FriendContactCycleRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToCycle: () -> Unit,
    viewModel: FriendContactViewModel = hiltViewModel(),
) {
    FriendContactCycleScreen(viewModel.contacts)
}

@Composable
fun FriendContactCycleScreen(contacts: List<FriendContactUIModel>) {
    NearFrame(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(horizontal = 24.dp),
    ) {
        ContactCycleTopAppBar()

        Spacer(modifier = Modifier.size(24.dp))

        ContactCycleHeader()

        Spacer(modifier = Modifier.size(40.dp))

        ContactLoadContent(contacts)

        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun ContactCycleTopAppBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
    ) {
        Text(
            text = "챙길사람 불러오기",
            style = NearTheme.typography.B1_16_BOLD,
        )

        Text(
            text = "1/2",
            style =
                NearTheme.typography.B2_14_MEDIUM.copy(
                    color = NearTheme.colors.GRAY01_888888,
                ),
        )
    }
}

@Composable
private fun ContactCycleHeader() {
    Image(
        painter = painterResource(R.drawable.img_100_character_default),
        contentDescription = null,
    )

    Spacer(modifier = Modifier.size(8.dp))

    Text(
        text = "가까워지고 싶은 사람\n10명까지 선택해주세요",
        style = NearTheme.typography.H1_24_MEDIUM,
    )

    Spacer(modifier = Modifier.size(12.dp))

    Text(
        text = "먼저, 더 가까워지고 싶은\n소중한 사람만 선택해보세요.",
        style =
            NearTheme.typography.B1_16_MEDIUM.copy(
                color = NearTheme.colors.GRAY01_888888,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun FriendContactCycleScreenPreview() {
    // 짱구 관련 더미데이터 생성
    val contacts =
        listOf(
            FriendContactUIModel(
                id = 1,
                name = "신짱구",
                photoUri = null,
            ),
        )

    NearTheme {
        FriendContactCycleScreen(
            contacts = contacts,
        )
    }
}
