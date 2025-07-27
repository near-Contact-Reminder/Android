package com.alarmy.near.presentation.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearLineTypeButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: String,
) {
    NearBasicButton(
        modifier = modifier,
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = NearTheme.colors.WHITE_FFFFFF,
                contentColor = NearTheme.colors.BLACK_1A1A1A,
                disabledContainerColor = NearTheme.colors.WHITE_FFFFFF,
                disabledContentColor = NearTheme.colors.GRAY02_B7B7B7,
            ),
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = NearTheme.colors.GRAY02_B7B7B7),
        contentPadding = contentPadding,
    ) {
        Text(text = text, style = NearTheme.typography.B1_16_BOLD)
    }
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearLineEnabledButtonPreview() {
    Surface {
        NearLineTypeButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            enabled = true,
            text = "Button",
            onClick = {},
            contentPadding = PaddingValues(vertical = 17.dp),
        )
    }
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearLineDisabledButtonPreview() {
    Surface {
        NearLineTypeButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            enabled = false,
            text = "Button",
            onClick = {},
            contentPadding = PaddingValues(vertical = 17.dp),
        )
    }
}
