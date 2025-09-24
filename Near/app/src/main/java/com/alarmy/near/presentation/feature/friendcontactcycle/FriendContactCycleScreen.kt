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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleButtons
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleContent
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactLoadContent
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIEvent
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun FriendContactCycleRoute(
    onNavigateToHome: () -> Unit,
    viewModel: FriendContactViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // UI 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FriendContactUIEvent.MoveToNextStep -> viewModel.moveToNextStep()
                is FriendContactUIEvent.MoveToPreviousStep -> viewModel.moveToPreviousStep()
                is FriendContactUIEvent.LoadContacts -> viewModel.fetchContacts()
                is FriendContactUIEvent.DeselectContact -> viewModel.deselectContact(event.contactId)
                is FriendContactUIEvent.ToggleBulkSetting -> viewModel.toggleBulkSetting()
                is FriendContactUIEvent.OpenBottomSheet -> viewModel.openBottomSheet()
                is FriendContactUIEvent.CloseBottomSheet -> viewModel.closeBottomSheet()
                is FriendContactUIEvent.CompleteCycleSetting -> viewModel.completeCycleSetting(event.reminderInterval)
                is FriendContactUIEvent.SetContactCycle -> viewModel.setContactCycle(event.contactId, event.reminderInterval)
            }
        }
    }

    FriendContactCycleScreen(
        uiState = uiState,
        onMoveToNextStep = { viewModel.onEvent(FriendContactUIEvent.MoveToNextStep) },
        onMoveToPreviousStep = { viewModel.onEvent(FriendContactUIEvent.MoveToPreviousStep) },
        onDeselectContact = { contactId ->
            viewModel.onEvent(FriendContactUIEvent.DeselectContact(contactId))
        },
        onToggleBulkSetting = { viewModel.onEvent(FriendContactUIEvent.ToggleBulkSetting) },
        onOpenBottomSheet = { viewModel.onEvent(FriendContactUIEvent.OpenBottomSheet) },
        onCloseBottomSheet = { viewModel.onEvent(FriendContactUIEvent.CloseBottomSheet) },
        onCompleteCycleSetting = { reminderInterval ->
            viewModel.onEvent(FriendContactUIEvent.CompleteCycleSetting(reminderInterval))
        },
        onSetContactCycle = { contactId, reminderInterval ->
            viewModel.onEvent(FriendContactUIEvent.SetContactCycle(contactId, reminderInterval))
        },
        onNavigateToHome = onNavigateToHome,
    )
}

@Composable
fun FriendContactCycleScreen(
    uiState: FriendContactUIState,
    onMoveToNextStep: () -> Unit,
    onMoveToPreviousStep: () -> Unit,
    onDeselectContact: (String) -> Unit,
    onToggleBulkSetting: () -> Unit,
    onOpenBottomSheet: () -> Unit,
    onCloseBottomSheet: () -> Unit,
    onCompleteCycleSetting: (ReminderInterval) -> Unit,
    onSetContactCycle: (String, ReminderInterval) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    NearFrame(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(horizontal = 24.dp),
    ) {
        ContactCycleTopAppBar(
            pageIndex = uiState.currentStep.ordinal + 1,
            title = uiState.currentStep.appbarTitle,
        )

        Spacer(modifier = Modifier.size(24.dp))

        when (uiState.currentStep) {
            ContactCycleStep.LOAD_CONTACTS -> {
                ContactCycleHeader(
                    headerTitle = "가까워지고 싶은 사람\n10명까지 선택해주세요",
                    headerSubTitle = "먼저, 더 가까워지고 싶은\n소중한 사람만 선택해보세요.",
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactLoadContent(
                    contacts = uiState.contacts,
                    onDeselectContact = onDeselectContact,
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onNavigateToHome,
                    onRightButtonClick = onMoveToNextStep,
                    leftButtonText = "나중에 하기",
                    rightButtonText = "다음",
                    isRightButtonEnabled = uiState.contacts.isNotEmpty(),
                )
            }

            ContactCycleStep.SET_CYCLE -> {
                ContactCycleHeader(
                    headerTitle = "얼마나 자주\n챙기고 싶으세요?",
                    headerSubTitle = "사람별로 챙기고 싶은 주기를 설정해주세요.",
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactCycleContent(
                    contacts = uiState.contacts,
                    isBulkSettingEnabled = uiState.isBulkSettingEnabled,
                    isBottomSheetVisible = uiState.isBottomSheetVisible,
                    selectedCycle = uiState.selectedCycle,
                    onToggleBulkSetting = onToggleBulkSetting,
                    onOpenBottomSheet = onOpenBottomSheet,
                    onCloseBottomSheet = onCloseBottomSheet,
                    onCompleteCycleSetting = onCompleteCycleSetting,
                    onSetContactCycle = onSetContactCycle,
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onMoveToPreviousStep,
                    onRightButtonClick = onNavigateToHome,
                    leftButtonText = "이전",
                    rightButtonText = "완료",
                    isRightButtonEnabled = uiState.isAllContactsCycleSet,
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

    NearTheme {
        FriendContactCycleScreen(
            uiState =
                FriendContactUIState(
                    contacts = contacts,
                ),
            onMoveToNextStep = {},
            onMoveToPreviousStep = {},
            onDeselectContact = {},
            onToggleBulkSetting = {},
            onOpenBottomSheet = {},
            onCloseBottomSheet = {},
            onCompleteCycleSetting = {},
            onSetContactCycle = { _, _ -> },
            onNavigateToHome = {},
        )
    }
}
