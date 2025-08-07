package com.alarmy.near.presentation.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R

@Composable
fun AddContactButton(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable._icon_64_adduser),
        contentDescription =
            stringResource(
                R.string.home_add_contact,
            ),
    )
}

@Preview(widthDp = 48, heightDp = 48)
@Composable
fun AddContactButtonPreview() {
    AddContactButton()
}
