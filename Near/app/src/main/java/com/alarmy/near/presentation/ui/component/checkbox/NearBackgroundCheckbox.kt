package com.alarmy.near.presentation.ui.component.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R

@Composable
fun NearBackgroundCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Image(
        modifier =
            modifier.clickable(
                onClick = { onCheckedChange(!checked) },
            ),
        painter =
            if (checked) {
                painterResource(R.drawable.btn_checkbox_h1_on)
            } else {
                painterResource(R.drawable.btn_checkbox_h1_off)
            },
        contentDescription =
            if (checked) {
                stringResource(
                    R.string.content_description_checkbox_check,
                )
            } else {
                stringResource(
                    R.string.content_description_checkbox_un_check,
                )
            },
    )
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearBackgroundCheckboxPreview() {
    Surface {
        Row {
            NearBackgroundCheckbox(
                checked = true,
                onCheckedChange = {},
            )
            Spacer(modifier = Modifier.width(50.dp))
            NearBackgroundCheckbox(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}
