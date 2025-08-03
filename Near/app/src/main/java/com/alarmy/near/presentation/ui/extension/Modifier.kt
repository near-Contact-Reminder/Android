package com.alarmy.near.presentation.ui.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.onNoRippleClick(onClick: () -> Unit): Modifier =
    this then
        Modifier.clickable(
            onClick = {},
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        )
