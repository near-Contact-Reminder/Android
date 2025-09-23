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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleButtons
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleContent
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactLoadContent
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun FriendContactCycleRoute(
    onNavigateToHome: () -> Unit,
    viewModel: FriendContactViewModel = hiltViewModel(),
) {
    val currentStep by viewModel.currentStep.collectAsState()

    FriendContactCycleScreen(
        contacts = viewModel.contacts,
        currentStep = currentStep,
        onNextClick = viewModel::moveToNextStep,
        onLaterClick = viewModel::moveToPreviousStep,
    )
}

@Composable
fun FriendContactCycleScreen(
    contacts: List<FriendContactUIModel>,
    currentStep: ContactCycleStep,
    onNextClick: () -> Unit,
    onLaterClick: () -> Unit,
) {
    NearFrame(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(horizontal = 24.dp),
    ) {
        ContactCycleTopAppBar(
            pageIndex = currentStep.ordinal + 1,
            title = currentStep.appbarTitle,
        )

        Spacer(modifier = Modifier.size(24.dp))

        when (currentStep) {
            ContactCycleStep.LOAD_CONTACTS -> {
                ContactCycleHeader(
                    headerTitle = "가까워지고 싶은 사람\n10명까지 선택해주세요",
                    headerSubTitle = "먼저, 더 가까워지고 싶은\n소중한 사람만 선택해보세요.",
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactLoadContent(
                    contacts = contacts,
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onLaterClick,
                    onRightButtonClick = onNextClick,
                    leftButtonText = "나중에 하기",
                    rightButtonText = "다음",
                )
            }

            ContactCycleStep.SET_CYCLE -> {
                ContactCycleHeader(
                    headerTitle = "얼마나 자주\n챙기고 싶으세요?",
                    headerSubTitle = "사람별로 챙기고 싶은 주기를 설정해주세요.",
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactCycleContent(
                    contacts = contacts,
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onLaterClick,
                    onRightButtonClick = { /* TODO: 완료 로직 */ },
                    leftButtonText = "이전",
                    rightButtonText = "완료",
                )
            }
        }

        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun ContactCycleTopAppBar(
    pageIndex: Int,
    title: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
    ) {
        Text(
            text = title,
            style = NearTheme.typography.B1_16_BOLD,
        )

        Text(
            text = "$pageIndex/2",
            style =
                NearTheme.typography.B2_14_MEDIUM.copy(
                    color = NearTheme.colors.GRAY01_888888,
                ),
        )
    }
}

@Composable
private fun ContactCycleHeader(
    headerTitle: String,
    headerSubTitle: String,
) {
    Image(
        painter = painterResource(R.drawable.img_100_character_default),
        contentDescription = null,
    )

    Spacer(modifier = Modifier.size(8.dp))

    Text(
        text = headerTitle,
        style = NearTheme.typography.H1_24_MEDIUM,
    )

    Spacer(modifier = Modifier.size(12.dp))

    Text(
        text = headerSubTitle,
        style =
            NearTheme.typography.B1_16_MEDIUM.copy(
                color = NearTheme.colors.GRAY01_888888,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun FriendContactCycleScreenPreview() {
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

    var currentStep by remember { mutableStateOf(ContactCycleStep.LOAD_CONTACTS) }

    NearTheme {
        FriendContactCycleScreen(
            contacts = contacts,
            currentStep = currentStep,
            onNextClick = { currentStep = ContactCycleStep.SET_CYCLE },
            onLaterClick = { currentStep = ContactCycleStep.LOAD_CONTACTS },
        )
    }
}
