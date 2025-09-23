package com.alarmy.near.presentation.feature.myprofile.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.button.NearLineTypeButton
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearLogoutButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
) {
    NearLineTypeButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        text = stringResource(R.string.my_profile_logout),
        contentPadding = contentPadding,
    )
}

@Preview
@Composable
fun NearLogoutButtonPreview() {
    NearTheme {
        NearLogoutButton()
    }
}
