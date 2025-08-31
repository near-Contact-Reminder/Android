package com.alarmy.near.presentation.feature.friendprofileedittor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.Friend
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendprofileedittor.component.NearDatePicker
import com.alarmy.near.presentation.feature.friendprofileedittor.component.ReminderIntervalBottomSheet
import com.alarmy.near.presentation.feature.friendprofileedittor.dialog.EditorExitDialog
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.FriendProfileEditorUIEvent
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.FriendProfileEditorUIState
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.component.radiobutton.NearSmallRadioButton
import com.alarmy.near.presentation.ui.component.textfield.NearLimitedTextField
import com.alarmy.near.presentation.ui.component.textfield.NearTextField
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

@Composable
fun FriendProfileEditorRoute(
    viewModel: FriendProfileEditorViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit = {},
    onSuccessEdit: (Friend) -> Unit = {},
) {
    val friendProfileEditorUIState = viewModel.uiState.collectAsStateWithLifecycle()
    val warningDialogState = remember { mutableStateOf(false) }
    LaunchedEffect(viewModel.uiEvent) {
        launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    FriendProfileEditorUIEvent.WarningExit -> {
                        warningDialogState.value = true
                    }

                    FriendProfileEditorUIEvent.Exit -> {
                        warningDialogState.value = false
                        onClickBackButton()
                    }

                    is FriendProfileEditorUIEvent.FriendProfileEditFailure -> {
                        onShowErrorSnackBar(event.throwable)
                    }

                    FriendProfileEditorUIEvent.FriendProfileEditNetworkError -> {
                        onShowErrorSnackBar(IllegalStateException("네트워크 에러가 발생했습니다."))
                    }

                    is FriendProfileEditorUIEvent.FriendProfileEditSuccess -> {
                        onSuccessEdit(event.friend)
                    }
                }
            }
        }
    }
    FriendProfileEditorScreen(
        friendProfileEditorUIState = friendProfileEditorUIState.value,
        dialogState = warningDialogState.value,
        onClickBackButton = viewModel::onExit,
        onNameChanged = viewModel::onNameChanged,
        onRelationChanged = viewModel::onRelationChanged,
        onReminderIntervalChanged = viewModel::onRemindIntervalChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onAnniversaryNameChange = viewModel::onAnniversaryTitleChanged,
        onAnniversaryDateSelected = viewModel::onAnniversaryDateChanged,
        onRemoveAnniversary = viewModel::onRemoveAnniversary,
        onAddAnniversary = viewModel::onAddAnniversary,
        onMemoChanged = viewModel::onMemoChanged,
        onSubmit = viewModel::onSubmit,
        onEditorExit = onClickBackButton,
        onCloseDialog = { warningDialogState.value = false },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileEditorScreen(
    modifier: Modifier = Modifier,
    dialogState: Boolean = false,
    friendProfileEditorUIState: FriendProfileEditorUIState,
    onClickBackButton: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onRelationChanged: (Relation) -> Unit = {},
    onReminderIntervalChanged: (ReminderInterval) -> Unit = {},
    onBirthdayChanged: (Long) -> Unit = {},
    onAnniversaryNameChange: (index: Int, name: String) -> Unit = { _, _ -> },
    onAnniversaryDateSelected: (index: Int, dataTimeMillis: Long) -> Unit = { _, _ -> },
    onRemoveAnniversary: (index: Int) -> Unit = { _ -> },
    onAddAnniversary: () -> Unit = {},
    onMemoChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onEditorExit: () -> Unit = {},
    onCloseDialog: () -> Unit = {},
) {
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val showBottomSheet = remember { mutableStateOf(false) }
    if (showBottomSheet.value) {
        ReminderIntervalBottomSheet(onDismissRequest = {
            showBottomSheet.value = false
        }, onSelectReminderInterval = {
            onReminderIntervalChanged(it)
            showBottomSheet.value = false
        })
    }
    if (dialogState) {
        EditorExitDialog(
            onDismissRequest = {
                onCloseDialog()
            },
            onConfirm = {
                onEditorExit()
            },
        )
    }
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF),
    ) {
        if (friendProfileEditorUIState.anniversaries.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.padding(top = statusBarHeightDp))
                NearTopAppbar(
                    modifier = Modifier.padding(end = 24.dp),
                    title = "",
                    onClickBackButton = onClickBackButton,
                    menuButton = {
                        Text(
                            modifier =
                                Modifier.onNoRippleClick(onClick = {
                                    onSubmit()
                                }),
                            text = "완료",
                            style = NearTheme.typography.B1_16_BOLD,
                            color = NearTheme.colors.BLACK_1A1A1A,
                        )
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 20.dp),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text =
                                buildAnnotatedString {
                                    append("이름")
                                    withStyle(
                                        style =
                                            SpanStyle(
                                                color = NearTheme.colors.BLUE01_5AA2E9,
                                            ),
                                    ) {
                                        append("*")
                                    }
                                },
                            textAlign = TextAlign.Center,
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(55.dp))
                        NearTextField(
                            modifier = Modifier.weight(1f),
                            value = friendProfileEditorUIState.name.value,
                            onValueChange = {
                                onNameChanged(it)
                            },
                        )
                    }
                    if (friendProfileEditorUIState.name.error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "이름을 입력해주세요.",
                            style = NearTheme.typography.FC_12_MEDIUM,
                            color = NearTheme.colors.NEGATIVE_F04E4E,
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "관계",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(72.dp))
                        Row(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 35.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                NearSmallRadioButton(
                                    selected = friendProfileEditorUIState.relation == Relation.FRIEND,
                                    onClick = {
                                        onRelationChanged(Relation.FRIEND)
                                    },
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.friend_profile_editor_relation_freind),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                NearSmallRadioButton(
                                    selected = friendProfileEditorUIState.relation == Relation.FAMILY,
                                    onClick = {
                                        onRelationChanged(Relation.FAMILY)
                                    },
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.friend_profile_editor_relation_family),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                NearSmallRadioButton(
                                    selected = friendProfileEditorUIState.relation == Relation.ACQUAINTANCE,
                                    onClick = { onRelationChanged(Relation.ACQUAINTANCE) },
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.friend_profile_editor_relation_acquaintance),
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(33.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "연락 주기",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(35.dp))
                        Surface(
                            modifier =
                                modifier
                                    .weight(1f)
                                    .onNoRippleClick({
                                        showBottomSheet.value = true
                                    }),
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
                                    Modifier.padding(
                                        start = 16.dp,
                                        end = 12.dp,
                                        top = 14.dp,
                                        bottom = 14.dp,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text =
                                        stringResource(friendProfileEditorUIState.contactFrequency.reminderInterval.labelRes) +
                                            "(${friendProfileEditorUIState.contactFrequency.dayOfWeek} 마다)",
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_24_down),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val birthdayDatePickerState = remember { mutableStateOf(false) }
                        val datePickerState =
                            rememberDatePickerState()
                        if (birthdayDatePickerState.value) {
                            NearDatePicker(
                                datePickerState = datePickerState,
                                onDismiss = { birthdayDatePickerState.value = false },
                                onDateSelected = {
                                    it?.let {
                                        onBirthdayChanged(it)
                                    }
                                },
                            )
                        }
                        Text(
                            "생일",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(62.dp))
                        Surface(
                            modifier =
                                modifier
                                    .weight(1f),
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
                                        .padding(
                                            start = 16.dp,
                                            end = 12.dp,
                                            top = 14.dp,
                                            bottom = 14.dp,
                                        ).onNoRippleClick({
                                            birthdayDatePickerState.value = true
                                        }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    friendProfileEditorUIState.birthday.value ?: "날짜 선택",
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_24_down),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "기념일",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Text(
                            modifier =
                                Modifier.onNoRippleClick(onClick = {
                                    onAddAnniversary()
                                }),
                            text = "추가하기",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.BLUE01_5AA2E9,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            items(
                count = friendProfileEditorUIState.anniversaries.size,
            ) { index ->
                Column(
                    modifier =
                        Modifier
                            .background(color = NearTheme.colors.BG02_F4F9FD)
                            .padding(
                                PaddingValues(
                                    top = 20.dp,
                                    bottom = 32.dp,
                                    start = 24.dp,
                                    end = 20.dp,
                                ),
                            ),
                ) {
                    val anniversaryDatePickerState = remember { mutableStateOf(false) }
                    val datePickerState =
                        rememberDatePickerState()
                    if (anniversaryDatePickerState.value) {
                        NearDatePicker(
                            datePickerState = datePickerState,
                            onDismiss = { anniversaryDatePickerState.value = false },
                            onDateSelected = {
                                it?.let {
                                    onAnniversaryDateSelected(index, it)
                                }
                            },
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "기념일 이름",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(23.dp))
                        NearTextField(
                            modifier = Modifier.weight(1f),
                            value = friendProfileEditorUIState.anniversaries[index].title.value,
                            onValueChange = {
                                onAnniversaryNameChange(index, it)
                            },
                        )
                    }
                    if (friendProfileEditorUIState.anniversaries[index].title.error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "이름을 입력해주세요.",
                            style = NearTheme.typography.FC_12_MEDIUM,
                            color = NearTheme.colors.NEGATIVE_F04E4E,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "날짜",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                        Spacer(modifier = Modifier.width(62.dp))
                        Surface(
                            modifier =
                                modifier
                                    .weight(1f)
                                    .onNoRippleClick(onClick = {
                                        anniversaryDatePickerState.value = true
                                    }),
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
                                    Modifier.padding(
                                        start = 16.dp,
                                        end = 12.dp,
                                        top = 14.dp,
                                        bottom = 14.dp,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    friendProfileEditorUIState.anniversaries[index].date.value
                                        ?: "날짜 선택",
                                    style = NearTheme.typography.B2_14_MEDIUM,
                                    color = NearTheme.colors.BLACK_1A1A1A,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_24_down),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .onNoRippleClick(
                                    onClick = {
                                        onRemoveAnniversary(index)
                                    },
                                ),
                        textAlign = TextAlign.End,
                        text = "삭제하기",
                        textDecoration = TextDecoration.Underline,
                        color = NearTheme.colors.GRAY01_888888,
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "메모",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
                Spacer(modifier = Modifier.width(23.dp))
                NearLimitedTextField(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(180.dp),
                    value = friendProfileEditorUIState.memo.value ?: "",
                    onValueChange = {
                        onMemoChanged(it)
                    },
                    placeHolderText =
                        "꼭 기억해야 할 내용을 기록해보세요.\n" +
                            "예) 날생선 X, 작년 생일에\n" +
                            "키링 선물함 등",
                    maxTextCount = 100,
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendProfileEditorScreenPreview() {
    NearTheme {
        FriendProfileEditorScreen(
            friendProfileEditorUIState =
                FriendProfileEditorUIState(
                    contactFrequency =
                        ContactFrequency(
                            reminderInterval = ReminderInterval.EVERY_DAY,
                            dayOfWeek = "2025-01-01",
                        ),
                ),
        )
    }
}
