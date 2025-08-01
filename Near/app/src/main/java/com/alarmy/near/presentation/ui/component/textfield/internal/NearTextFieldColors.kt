package com.alarmy.near.presentation.ui.component.textfield.internal

import android.annotation.SuppressLint
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.alarmy.near.presentation.ui.theme.NearTheme

@SuppressLint("ComposableNaming")
@Composable
fun NearTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = NearTheme.colors.BLUE02_8ACCFF,
        focusedTextColor = NearTheme.colors.BLACK_1A1A1A,
        focusedContainerColor = NearTheme.colors.WHITE_FFFFFF,
        unfocusedTextColor = NearTheme.colors.BLACK_1A1A1A,
        unfocusedBorderColor = NearTheme.colors.GRAY03_EBEBEB,
        unfocusedContainerColor = NearTheme.colors.WHITE_FFFFFF,
        disabledTextColor = NearTheme.colors.GRAY02_B7B7B7,
        disabledContainerColor = NearTheme.colors.GRAY04_F7F7F7,
        disabledBorderColor = NearTheme.colors.GRAY03_EBEBEB,
    )
