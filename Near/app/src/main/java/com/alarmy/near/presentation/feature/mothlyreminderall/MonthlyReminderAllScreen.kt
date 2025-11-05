package com.alarmy.near.presentation.feature.mothlyreminderall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.mothlyreminderall.components.MonthlyReminderComplete
import com.alarmy.near.presentation.feature.mothlyreminderall.components.MonthlyReminderEmpty
import com.alarmy.near.presentation.feature.mothlyreminderall.components.MonthlyReminderFriendCard
import com.alarmy.near.presentation.feature.mothlyreminderall.components.RecordSuccessDialog
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIEvent
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

@Composable
fun MonthlyReminderAllRoute(
    viewModel: MonthlyReminderAllViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val recordSuccessDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiEvent) {
        launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is MonthlyReminderAllUIEvent.NetworkError -> {
                        onShowErrorSnackBar(
                            IllegalStateException(
                                context.getString(R.string.monthly_reminder_all_network_error),
                            ),
                        )
                    }

                    is MonthlyReminderAllUIEvent.RecordFriendShipSuccess -> {
                        recordSuccessDialogState.value = true
                    }
                }
            }
        }
    }

    MonthlyReminderAllScreen(
        uiState = uiState.value,
        recordSuccessDialogState = recordSuccessDialogState.value,
        onNavigateBack = onNavigateBack,
        onRecordFriendShip = viewModel::onRecordFriendShip,
        onDismissRecordSuccessDialog = {
            recordSuccessDialogState.value = false
        },
    )
}

@Composable
internal fun MonthlyReminderAllScreen(
    modifier: Modifier = Modifier,
    uiState: MonthlyReminderAllUIState,
    recordSuccessDialogState: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onRecordFriendShip: (String) -> Unit = {},
    onDismissRecordSuccessDialog: () -> Unit = {},
) {
    if (recordSuccessDialogState) {
        RecordSuccessDialog(onDismiss = onDismissRecordSuccessDialog)
    }

    NearFrame(
        modifier =
            modifier
                .background(NearTheme.colors.WHITE_FFFFFF),
    ) {
        NearTopAppbar(
            title = stringResource(R.string.monthly_reminder_all_title),
            onClickBackButton = onNavigateBack,
        )

        when (uiState) {
            is MonthlyReminderAllUIState.Loading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = NearTheme.colors.BLUE01_5AA2E9)
                }
            }

            is MonthlyReminderAllUIState.Empty -> {
                MonthlyReminderEmpty()
            }

            is MonthlyReminderAllUIState.Success -> {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                ) {
                    items(
                        items = uiState.monthlyReminders,
                        key = { "monthly_${it.friendId}" },
                    ) { reminder ->
                        Spacer(modifier = Modifier.size(16.dp))

                        MonthlyReminderFriendCard(
                            reminder = reminder,
                            onRecordClick = onRecordFriendShip,
                        )
                    }

                    if (uiState.hasCompletedReminders) {
                        item {
                            Spacer(modifier = Modifier.size(32.dp))

                            Text(
                                text = stringResource(R.string.monthly_reminder_all_completed_section_title),
                                style = NearTheme.typography.B2_14_BOLD,
                                color = NearTheme.colors.BLACK_1A1A1A,
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        items(
                            items = uiState.completedReminders,
                            key = { "completed_${it.friendId}" },
                        ) { reminder ->
                            MonthlyReminderComplete(reminder = reminder)
                        }
                    }
                }

                Spacer(modifier = Modifier.size(80.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MonthlyReminderAllScreenPreview() {
    NearTheme {
        MonthlyReminderAllScreen(
            uiState =
                MonthlyReminderAllUIState.Success(
                    monthlyReminders =
                        listOf(
                            MonthlyReminderUIModel(
                                friendId = "1",
                                name = "신짱구",
                                imageRes = R.drawable.icon_visual_cake,
                                descriptionRes = R.string.monthly_reminder_all_type_birthday_description,
                                nextContactAt = "2025-11-05",
                                daysUntilNextContact = "D-4",
                            ),
                            MonthlyReminderUIModel(
                                friendId = "2",
                                name = "김철수",
                                imageRes = R.drawable.icon_visual_mail,
                                descriptionRes = R.string.monthly_reminder_all_type_message_description,
                                nextContactAt = "2025-11-01",
                                daysUntilNextContact = "D-DAY",
                            ),
                        ),
                    completedReminders =
                        listOf(
                            MonthlyReminderUIModel(
                                friendId = "3",
                                name = "흰둥이",
                                imageRes = R.drawable.icon_visual_24_heart,
                                descriptionRes = R.string.monthly_reminder_all_type_anniversary_description,
                                nextContactAt = "2025-10-15",
                                daysUntilNextContact = "D+16",
                            ),
                        ),
                    hasCompletedReminders = true,
                ),
        )
    }
}

@Preview
@Composable
private fun MonthlyReminderAllScreenEmptyPreview() {
    NearTheme {
        MonthlyReminderAllScreen(
            uiState = MonthlyReminderAllUIState.Empty,
        )
    }
}

@Preview
@Composable
private fun MonthlyReminderAllScreenLoadingPreview() {
    NearTheme {
        MonthlyReminderAllScreen(
            uiState = MonthlyReminderAllUIState.Loading,
        )
    }
}
