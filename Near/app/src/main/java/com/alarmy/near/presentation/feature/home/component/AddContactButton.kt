package com.alarmy.near.presentation.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

private const val MAX_WIDTH_OF_NAME_TEXT = 97

@Composable
fun AddContactButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.onNoRippleClick(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddContactImage()
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(MAX_WIDTH_OF_NAME_TEXT.dp),
            text = stringResource(R.string.home_add_people_text),
            textAlign = TextAlign.Center,
            style = NearTheme.typography.B2_14_MEDIUM,
            color = Color(0xff222222),
        )
    }
}

@Composable
fun AddInitialContactButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .onNoRippleClick(
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddContactImage()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.home_add_contact_description),
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.3f),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddContactButtonPreview() {
    AddContactButton()
}

@Preview(showBackground = true)
@Composable
fun AddInitialContactButtonPreview() {
    AddInitialContactButton()
}
