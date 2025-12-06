package com.alarmy.near.presentation.feature.friendprofileedittor.component

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearDatePicker(
    datePickerState: DatePickerState =
        rememberDatePickerState(),
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        colors =
            DatePickerDefaults.colors().copy(
                containerColor = NearTheme.colors.WHITE_FFFFFF,
            ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = NearTheme.colors.BLUE01_5AA2E9,
                    ),
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = NearTheme.colors.BLUE01_5AA2E9,
                    ),
            ) {
                Text("닫기")
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            dateFormatter =
                remember
                { DatePickerDefaults.dateFormatter() },
            colors =
                DatePickerDefaults.colors().copy(
                    containerColor = NearTheme.colors.WHITE_FFFFFF,
                    selectedDayContainerColor = NearTheme.colors.BLUE01_5AA2E9,
                    selectedDayContentColor = NearTheme.colors.WHITE_FFFFFF,
                    todayDateBorderColor = NearTheme.colors.BLUE01_5AA2E9,
                    todayContentColor = NearTheme.colors.BLUE01_5AA2E9,
                    currentYearContentColor = NearTheme.colors.BLUE01_5AA2E9,
                    selectedYearContentColor = NearTheme.colors.WHITE_FFFFFF,
                    selectedYearContainerColor = NearTheme.colors.BLUE01_5AA2E9,
                ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DatePickerDockedPreview() {
    NearTheme {
        NearDatePicker(
            onDateSelected = {},
            onDismiss = {},
        )
    }
}
