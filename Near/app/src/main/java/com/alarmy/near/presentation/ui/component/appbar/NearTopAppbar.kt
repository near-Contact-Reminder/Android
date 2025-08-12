package com.alarmy.near.presentation.ui.component.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearTopAppbar(
    modifier: Modifier = Modifier,
    title: String,
    onClickBackButton: () -> Unit = {},
    isMenuVisible: Boolean = false,
    onClickMenuButton: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.clickable(onClick = onClickBackButton),
                painter = painterResource(R.drawable.ic_back_32_black),
                contentDescription =
                    stringResource(
                        R.string.common_back_button_description,
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                title,
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
        }
        if (isMenuVisible) {
            Image(
                modifier = Modifier.clickable(onClick = onClickMenuButton),
                painter = painterResource(R.drawable.ic_32_menu),
                contentDescription = stringResource(R.string.common_menu_button_description),
            )
        }
    }
}
