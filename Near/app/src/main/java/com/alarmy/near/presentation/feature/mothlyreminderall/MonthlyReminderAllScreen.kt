package com.alarmy.near.presentation.feature.mothlyreminderall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.mothlyreminderall.components.MonthlyReminderEmpty
import com.alarmy.near.presentation.feature.mothlyreminderall.components.MonthlyReminderFriendCard
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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent) {
        launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is MonthlyReminderAllUIEvent.NetworkError -> {
                        onShowErrorSnackBar(IllegalStateException("네트워크 에러가 발생했습니다."))
                    }
                }
            }
        }
    }

    MonthlyReminderAllScreen(
        uiState = uiState.value,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
internal fun MonthlyReminderAllScreen(
    modifier: Modifier = Modifier,
    uiState: MonthlyReminderAllUIState,
    onNavigateBack: () -> Unit = {},
) {
    NearFrame(
        modifier = modifier.background(NearTheme.colors.WHITE_FFFFFF),
    ) {
        NearTopAppbar(
            title = "이번달 챙길 사람",
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

            is MonthlyReminderAllUIState.Success -> {
                if (uiState.monthlyReminders.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        MonthlyReminderEmpty()
                    }
                } else {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(
                            items = uiState.monthlyReminders,
                            key = { it.friendId },
                        ) { reminder ->
                            MonthlyReminderFriendCard(reminder = reminder)
                        }
                    }
                }
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
                                description = "생일 축하 전해요",
                                nextContactAt = "2025-11-05",
                                daysUntilNextContact = "D-4",
                            ),
                            MonthlyReminderUIModel(
                                friendId = "2",
                                name = "김철수",
                                imageRes = R.drawable.icon_visual_mail,
                                description = "가볍게 안부인사 전해요",
                                nextContactAt = "2025-11-01",
                                daysUntilNextContact = "D-DAY",
                            ),
                            MonthlyReminderUIModel(
                                friendId = "3",
                                name = "흰둥이",
                                imageRes = R.drawable.icon_visual_24_heart,
                                description = "소중한 날 마음을 전해요",
                                nextContactAt = "2025-11-10",
                                daysUntilNextContact = "D-9",
                            ),
                        ),
                ),
        )
    }
}

@Preview
@Composable
private fun MonthlyReminderAllScreenEmptyPreview() {
    NearTheme {
        MonthlyReminderAllScreen(
            uiState = MonthlyReminderAllUIState.Success(monthlyReminders = emptyList()),
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
