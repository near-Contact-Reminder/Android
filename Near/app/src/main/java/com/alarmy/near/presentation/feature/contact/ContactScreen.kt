package com.alarmy.near.presentation.feature.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.textfield.NearSearchTextField
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ContactRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
    ContactScreen()
}

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    NearFrame(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 20.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                    ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.contact_title_text),
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
            Image(
                painter = painterResource(R.drawable.ic_close_32_black),
                contentDescription = stringResource(R.string.contact_close_screen),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        NearSearchTextField(
            placeHolderText = stringResource(R.string.context_search_placeholder),
            modifier = Modifier.padding(horizontal = 20.dp),
            value = "",
            onValueChange = onSearchTextChange,
            onSearchClick = onSearchClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    NearTheme {
        ContactScreen { }
    }
}
