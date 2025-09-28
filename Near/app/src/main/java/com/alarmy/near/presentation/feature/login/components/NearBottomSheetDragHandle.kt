package com.alarmy.near.presentation.feature.login.components

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NearBottomSheetDragHandle() {
    BottomSheetDefaults.DragHandle(
        color = NearTheme.colors.BLACK_1A1A1A.copy(
            alpha = 0.1f,
        ),
        width = 32.dp,
        height = 6.dp,
    )
}
