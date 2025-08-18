package com.alarmy.near.presentation.feature.friendprofileedittor.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.ui.component.button.NearLineTypeButton
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.component.checkbox.NearCheckbox
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderIntervalBottomSheet(
    modifier: Modifier = Modifier,
    selectedReminderInterval: ReminderInterval = ReminderInterval.WEEKLY,
    onSelectReminderInterval: (ReminderInterval) -> Unit = {},
    sheetState: SheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
    onDismissRequest: () -> Unit = {},
) {
    val initialReminderInterval = remember { selectedReminderInterval }

    val tempSelected = remember { mutableStateOf(initialReminderInterval) }
    ModalBottomSheet(
        modifier = modifier,
        containerColor = NearTheme.colors.WHITE_FFFFFF,
        sheetState = sheetState,
        onDismissRequest = {
            tempSelected.value = initialReminderInterval
            onDismissRequest()
        },
        dragHandle = {
            Surface(
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
                color = Color.Black.copy(alpha = 0.1f),
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(36.dp)
                            .height(5.dp),
                )
            }
        },
    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = "주기 설정",
            style = NearTheme.typography.B1_16_BOLD,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            color = NearTheme.colors.BG02_F4F9FD,
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    buildAnnotatedString {
                        append("매주")
                        withStyle(
                            style =
                                SpanStyle(
                                    color = NearTheme.colors.BLUE01_5AA2E9,
                                    fontWeight = FontWeight.Bold,
                                ),
                        ) {
                            append(" 화요일")
                        }
                    },
                    color = NearTheme.colors.BLACK_1A1A1A,
                    style = NearTheme.typography.B2_14_MEDIUM,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VerticalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.height(28.dp),
                        color = NearTheme.colors.BLACK_1A1A1A.copy(0.1f),
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "다음 주기 : 4/8 화",
                        style = NearTheme.typography.B2_14_MEDIUM,
                        color = NearTheme.colors.GRAY01_888888,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(28.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            items(
                count = ReminderInterval.entries.size,
                key = { index -> ReminderInterval.entries[index] },
            ) { item ->
                val reminderInterval = ReminderInterval.entries[item]
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 14.dp)
                            .onNoRippleClick(onClick = {
                                tempSelected.value = reminderInterval
                            }),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(reminderInterval.labelRes),
                        style =
                            if (reminderInterval == tempSelected.value) {
                                NearTheme.typography.B1_16_BOLD
                            } else {
                                NearTheme.typography.B1_16_MEDIUM
                            },
                        color = NearTheme.colors.BLACK_1A1A1A,
                    )
                    NearCheckbox(
                        checked = tempSelected.value == reminderInterval,
                        onCheckedChange = {},
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            NearLineTypeButton(
                modifier = Modifier.weight(1f),
                text = "취소",
                onClick = {
                    tempSelected.value = initialReminderInterval
                    onDismissRequest()
                },
                contentPadding = PaddingValues(vertical = 17.dp),
                enabled = true,
            )
            Spacer(modifier = Modifier.width(7.dp))
            NearSolidTypeButton(
                modifier = Modifier.weight(1f),
                text = "확인",
                onClick = {
                    onSelectReminderInterval(tempSelected.value)
                },
                enabled = true,
                contentPadding = PaddingValues(vertical = 17.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ReminderIntervalBottomSheetPreview() {
    NearTheme {
        ReminderIntervalBottomSheet(
            sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded),
        )
    }
}
