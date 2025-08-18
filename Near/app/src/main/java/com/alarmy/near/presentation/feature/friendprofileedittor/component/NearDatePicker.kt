package com.alarmy.near.presentation.feature.friendprofileedittor.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
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
import com.alarmy.near.presentation.ui.theme.NearColor
import com.alarmy.near.presentation.ui.theme.NearTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearDatePicker(
    datePickerState: DatePickerState =
        rememberDatePickerState(),
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors().copy(
            containerColor = NearTheme.colors.WHITE_FFFFFF,
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
    ) {
        DatePicker(state = datePickerState, title = null, headline = null, showModeToggle = false,
            dateFormatter =
                remember
                { DatePickerDefaults.dateFormatter() },
                colors = DatePickerDefaults.colors().copy(
                containerColor = NearTheme.colors.WHITE_FFFFFF,
            ))
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
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
