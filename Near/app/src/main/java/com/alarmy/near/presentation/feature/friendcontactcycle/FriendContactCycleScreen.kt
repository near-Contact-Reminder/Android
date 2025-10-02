package com.alarmy.near.presentation.feature.friendcontactcycle

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.alarmy.near.R
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.model.contact.Contact
import com.alarmy.near.presentation.feature.contact.navigation.CONTACT_SELECTION_COMPLETE_KEY
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleButtons
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleContent
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactLoadContent
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactPermissionDeniedDialog
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIEvent
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.permission.ContactPermissionRequester
import com.alarmy.near.presentation.ui.theme.NearTheme
import com.alarmy.near.presentation.ui.util.AppSettingsUtil

@Composable
internal fun FriendContactCycleRoute(
    navBackStackEntry: NavBackStackEntry,
    onNavigateToHome: () -> Unit,
    onNavigateToContact: () -> Unit = {},
    viewModel: FriendContactViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navigation에서 전달된 연락처 데이터 관찰
    LaunchedEffect(Unit) {
        navBackStackEntry.savedStateHandle
            .get<List<Contact>>(
                CONTACT_SELECTION_COMPLETE_KEY,
            )?.let { contacts ->
                viewModel.addSelectedContacts(contacts)
                navBackStackEntry.savedStateHandle.remove<List<Contact>>(
                    CONTACT_SELECTION_COMPLETE_KEY,
                )
            }
    }

    // UI 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FriendContactUIEvent.MoveToNextStep -> viewModel.moveToNextStep()
                is FriendContactUIEvent.MoveToPreviousStep -> viewModel.moveToPreviousStep()
                is FriendContactUIEvent.AddSelectedContacts -> viewModel.addSelectedContacts(event.contacts)
                is FriendContactUIEvent.DeselectContact -> viewModel.deselectContact(event.contactId)
                is FriendContactUIEvent.ToggleBulkSetting -> viewModel.toggleBulkSetting()
                is FriendContactUIEvent.OpenBottomSheet -> viewModel.openBottomSheet()
                is FriendContactUIEvent.OpenIndividualBottomSheet ->
                    viewModel.openIndividualBottomSheet(
                        event.contactId,
                    )

                is FriendContactUIEvent.CloseBottomSheet -> viewModel.closeBottomSheet()
                is FriendContactUIEvent.CompleteCycleSetting -> viewModel.completeCycleSetting(event.reminderInterval)
                is FriendContactUIEvent.CompleteFriendInit -> {
                    viewModel.completeFriendInit()
                }

                is FriendContactUIEvent.NavigateToHome -> {
                    onNavigateToHome()
                }
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
        onOpenIndividualBottomSheet = { contactId ->
            viewModel.onEvent(FriendContactUIEvent.OpenIndividualBottomSheet(contactId))
        },
        onCloseBottomSheet = { viewModel.onEvent(FriendContactUIEvent.CloseBottomSheet) },
        onCompleteCycleSetting = { reminderInterval ->
            viewModel.onEvent(FriendContactUIEvent.CompleteCycleSetting(reminderInterval))
        },
        onCompleteFriendInit = { viewModel.onEvent(FriendContactUIEvent.CompleteFriendInit) },
        onNavigateToHome = onNavigateToHome,
        onNavigateToContact = onNavigateToContact,
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
    onOpenIndividualBottomSheet: (String) -> Unit,
    onCloseBottomSheet: () -> Unit,
    onCompleteCycleSetting: (ReminderInterval) -> Unit,
    onCompleteFriendInit: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToContact: () -> Unit = {},
) {
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    var onRequestPermission: (() -> Unit)? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    ContactPermissionRequester(
        onGranted = {
            onNavigateToContact()
        },
        onDenied = { requestPermission ->
            onRequestPermission = requestPermission
        },
        onShowRationale = { requestPermission ->
            onRequestPermission = requestPermission
        },
        onPermissionDenied = {
            showPermissionDeniedDialog = true
        },
    )

    NearFrame(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(horizontal = 24.dp),
    ) {
        if (uiState.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(NearTheme.colors.WHITE_FFFFFF),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = NearTheme.colors.BLUE01_5AA2E9,
                )
            }
        }

        ContactCycleTopAppBar(
            pageIndex = uiState.currentStep.ordinal + 1,
            title = stringResource(uiState.currentStep.appbarTitleResId),
        )

        Spacer(modifier = Modifier.size(24.dp))

        when (uiState.currentStep) {
            ContactCycleStep.LOAD_CONTACTS -> {
                ContactCycleHeader(
                    headerTitle = stringResource(R.string.friend_contact_cycle_header_title),
                    headerSubTitle = stringResource(R.string.friend_contact_cycle_header_subtitle),
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactLoadContent(
                    contacts = uiState.contacts,
                    onDeselectContact = onDeselectContact,
                    onContactLoadClick = {
                        // NearListModuleBackground 클릭 시 권한 요청
                        onRequestPermission?.invoke()
                    },
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onNavigateToHome,
                    onRightButtonClick = onMoveToNextStep,
                    leftButtonText = stringResource(R.string.friend_contact_cycle_later_button),
                    rightButtonText = stringResource(R.string.friend_contact_cycle_next_button),
                    isRightButtonEnabled = uiState.contacts.isNotEmpty(),
                )
            }

            ContactCycleStep.SET_CYCLE -> {
                ContactCycleHeader(
                    headerTitle = stringResource(R.string.friend_contact_cycle_setting_header_title),
                    headerSubTitle = stringResource(R.string.friend_contact_cycle_setting_header_subtitle),
                )

                Spacer(modifier = Modifier.size(40.dp))

                ContactCycleContent(
                    contacts = uiState.contacts,
                    isBulkSettingEnabled = uiState.isBulkSettingEnabled,
                    isBottomSheetVisible = uiState.isBottomSheetVisible,
                    selectedCycle = uiState.selectedCycle,
                    onToggleBulkSetting = onToggleBulkSetting,
                    onOpenBottomSheet = onOpenBottomSheet,
                    onOpenIndividualBottomSheet = onOpenIndividualBottomSheet,
                    onCloseBottomSheet = onCloseBottomSheet,
                    onCompleteCycleSetting = onCompleteCycleSetting,
                )

                Spacer(modifier = Modifier.size(16.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onMoveToPreviousStep,
                    onRightButtonClick = onCompleteFriendInit,
                    leftButtonText = stringResource(R.string.friend_contact_cycle_previous_button),
                    rightButtonText = stringResource(R.string.friend_contact_cycle_complete_button),
                    isRightButtonEnabled = uiState.isAllContactsCycleSet,
                )
            }
        }

        Spacer(modifier = Modifier.size(24.dp))
    }

    // 권한 거부 다이얼로그 표시
    if (showPermissionDeniedDialog) {
        ContactPermissionDeniedDialog(
            onDismiss = {
                showPermissionDeniedDialog = false
            },
            onGoToSettings = {
                showPermissionDeniedDialog = false
                AppSettingsUtil.openAppSettings(context)
            },
        )
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
            text = stringResource(R.string.friend_contact_cycle_page_format, pageIndex),
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
            ),
            FriendContactUIModel(
                id = 2,
                name = "철수",
            ),
            FriendContactUIModel(
                id = 3,
                name = "유리",
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
            onOpenIndividualBottomSheet = {},
            onCloseBottomSheet = {},
            onCompleteCycleSetting = {},
            onNavigateToHome = {},
            onNavigateToContact = {},
            onCompleteFriendInit = {},
        )
    }
}
