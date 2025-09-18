package com.alarmy.near.presentation.feature.login.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.checkbox.NearCheckbox
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun TermsAgreementItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showDivider: Boolean,
) {
    Row(
        modifier = Modifier.padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NearCheckbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = text,
            style = NearTheme.typography.B2_14_MEDIUM,
        )
    }

    if (showDivider) {
        HorizontalDivider(
            color = NearTheme.colors.GRAY03_EBEBEB,
        )
    }
}
