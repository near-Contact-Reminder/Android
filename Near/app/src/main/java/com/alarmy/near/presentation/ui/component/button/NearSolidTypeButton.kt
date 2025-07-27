package com.alarmy.near.presentation.ui.component.button

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
fun NearSolidTypeButton(
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
                containerColor = NearTheme.colors.BLUE01_5AA2E9,
                contentColor = NearTheme.colors.WHITE_FFFFFF,
                disabledContainerColor = NearTheme.colors.GRAY02_B7B7B7,
                disabledContentColor = NearTheme.colors.WHITE_FFFFFF,
            ),
        enabled = enabled,
        contentPadding = contentPadding,
    ) {
        Text(text = text, style = NearTheme.typography.B1_16_BOLD)
    }
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearSolidEnabledButtonPreview() {
    Surface {
        NearSolidTypeButton(
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
fun NearSolidDisabledButtonPreview() {
    Surface {
        NearSolidTypeButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            enabled = false,
            text = "Button",
            onClick = {},
            contentPadding = PaddingValues(vertical = 17.dp),
        )
    }
}
