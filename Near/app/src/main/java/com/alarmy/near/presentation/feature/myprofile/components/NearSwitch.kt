package com.alarmy.near.presentation.feature.myprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Switch(
        modifier = modifier.size(width = 48.dp, height = 26.dp),
        colors =
            SwitchDefaults.colors(
                checkedTrackColor = NearTheme.colors.BLUE01_5AA2E9,
                checkedThumbColor = NearTheme.colors.WHITE_FFFFFF,
                uncheckedTrackColor = NearTheme.colors.GRAY03_EBEBEB,
                uncheckedThumbColor = NearTheme.colors.WHITE_FFFFFF,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
            ),
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            Box(
                modifier =
                    Modifier
                        .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) { }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun NearSwitchPreview() {
    NearTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 선택된 상태
            NearSwitch(
                checked = true,
                onCheckedChange = {},
            )

            // 선택 안된 상태
            NearSwitch(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}
