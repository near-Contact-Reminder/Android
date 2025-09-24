package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.ui.component.checkbox.NearCheckbox
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import com.alarmy.near.utils.extensions.DateExtension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycleSettingBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onComplete: (ReminderInterval) -> Unit = {},
    currentSelectedInterval: ReminderInterval? = null,
    modifier: Modifier = Modifier,
) {
    // 선택된 주기 상태 관리 (기존 선택값이 있으면 그것을 사용, 없으면 매주를 기본값으로)
    var selectedInterval by remember(isVisible) { 
        mutableStateOf<ReminderInterval?>(currentSelectedInterval ?: ReminderInterval.EVERY_WEEK) 
    }

    if (isVisible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.1f),
                    width = 36.dp,
                    height = 5.dp,
                )
            },
            modifier = modifier,
            containerColor = NearTheme.colors.WHITE_FFFFFF,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "주기 설정",
                    style = NearTheme.typography.B1_16_BOLD,
                )

                Spacer(modifier = Modifier.size(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NearTheme.colors.BG02_F4F9FD)
                            .padding(vertical = 18.dp, horizontal = 20.dp),
                ) {
                    Text(
                        text =
                            buildAnnotatedString {
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = NearTheme.colors.BLACK_1A1A1A,
                                            fontWeight = NearTheme.typography.B2_14_MEDIUM.fontWeight,
                                        ),
                                ) {
                                    append("매주 ")
                                }
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = NearTheme.colors.BLUE01_5AA2E9,
                                            fontWeight = NearTheme.typography.B2_14_BOLD.fontWeight,
                                        ),
                                ) {
                                    append(DateExtension.getTodayDayOfWeekInKorean())
                                }
                            },
                        style = NearTheme.typography.B2_14_MEDIUM,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.size(20.dp))

                        VerticalDivider(
                            modifier =
                                Modifier
                                    .size(width = 1.dp, height = 28.dp),
                            color =
                                NearTheme.colors.BLACK_1A1A1A.copy(
                                    alpha = 0.1f,
                                ),
                        )

                        Spacer(modifier = Modifier.size(20.dp))

                        Text(
                            text = "다음 주기 : ${
                                selectedInterval?.let { DateExtension.getNextCycleDate(it) } 
                                    ?: DateExtension.getNextWeekSameDay()
                            }",
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                ReminderInterval.entries.forEach { interval ->
                    val isSelected = selectedInterval == interval

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .onNoRippleClick {
                                    if (!isSelected) {
                                        selectedInterval = interval
                                    }
                                }.padding(vertical = 15.dp),
                    ) {
                        Text(
                            text = stringResource(interval.labelRes),
                            style =
                                if (isSelected) {
                                    NearTheme.typography.B1_16_BOLD
                                } else {
                                    NearTheme.typography.B2_14_MEDIUM
                                },
                        )

                        if (isSelected) {
                            NearCheckbox(
                                checked = true,
                                onCheckedChange = { checked ->
                                    // 체크박스 클릭 시 해제되지 않도록 수정
                                    // 체크된 상태를 유지
                                },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(24.dp))

                ContactCycleButtons(
                    onLeftButtonClick = onDismiss,
                    onRightButtonClick = { 
                        selectedInterval?.let { interval ->
                            onComplete(interval)
                            onDismiss()
                        }
                    },
                    leftButtonText = "취소",
                    rightButtonText = "완료",
                )
                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Preview
@Composable
fun CycleSettingBottomSheetPreview() {
    NearTheme {
        CycleSettingBottomSheet(
            isVisible = true,
            onDismiss = {},
        )
    }
}
