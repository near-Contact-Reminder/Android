package com.alarmy.near.presentation.ui.component.radiobutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R

@Composable
fun NearSmallRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: (Boolean) -> Unit,
) {
    Image(
        modifier =
            modifier.clickable(
                onClick = { onClick(!selected) },
            ),
        painter =
            if (selected) {
                painterResource(R.drawable.btn_radio_h2_on)
            } else {
                painterResource(R.drawable.btn_radio_h2_off)
            },
        contentDescription = null,
    )
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearSmallRadioButtonPreview() {
    Surface {
        Row {
            NearSmallRadioButton(
                selected = true,
                onClick = {},
            )
            Spacer(modifier = Modifier.width(50.dp))
            NearSmallRadioButton(
                selected = false,
                onClick = {},
            )
        }
    }
}
