package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.button.NearLineTypeButton
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ContactCycleButtons() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        NearLineTypeButton(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            enabled = true,
            text = "나중에 하기",
            onClick = {},
        )

        Spacer(modifier = Modifier.size(7.dp))

        NearBasicButton(
            modifier = Modifier.weight(1f),
            onClick = {},
            enabled = true,
            contentPadding = PaddingValues(16.dp),
        ) {
            Text(
                text = "다음",
                style = NearTheme.typography.B1_16_BOLD,
            )
        }
    }
}
