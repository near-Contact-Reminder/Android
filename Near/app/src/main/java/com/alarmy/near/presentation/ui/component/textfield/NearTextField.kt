package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    placeHolderText: String = "",
    singleLine: Boolean = false,
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
    decorationBox: (@Composable (innerTextField: @Composable () -> Unit) -> Unit)? = null,
) {
    val colors =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NearTheme.colors.BLUE02_8ACCFF,
            focusedTextColor = NearTheme.colors.BLACK_1A1A1A,
            focusedContainerColor = NearTheme.colors.WHITE_FFFFFF,
            unfocusedTextColor = NearTheme.colors.BLACK_1A1A1A,
            unfocusedBorderColor = NearTheme.colors.GRAY03_EBEBEB,
            unfocusedContainerColor = NearTheme.colors.WHITE_FFFFFF,
            disabledTextColor = NearTheme.colors.GRAY02_B7B7B7,
            disabledContainerColor = NearTheme.colors.GRAY04_F7F7F7,
            disabledBorderColor = NearTheme.colors.GRAY03_EBEBEB,
        )

    BasicTextField(
        value = value,
        modifier = modifier,
        enabled = enabled,
        textStyle =
            NearTheme.typography.B2_14_MEDIUM.copy(
                color = if (enabled) colors.focusedTextColor else colors.disabledTextColor,
            ),
        cursorBrush = SolidColor(NearTheme.colors.BLACK_1A1A1A),
        onValueChange = onValueChange,
        decorationBox =
            decorationBox
                ?: { innerTextField ->
                    NearOutlinedTextFieldDecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = enabled,
                        singleLine = singleLine,
                        interactionSource = interactionSource,
                        colors = colors,
                        placeHolderText = placeHolderText,
                    )
                },
    )
}

@Preview(widthDp = 370, heightDp = 80, showBackground = true)
@Composable
fun NearUnFocusedTextFieldPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearTextField(
            modifier = Modifier.wrapContentHeight(),
            value = "",
            onValueChange = {},
            placeHolderText = "플레이스 홀더",
        )
    }
}

@Preview(widthDp = 370, heightDp = 80, showBackground = true)
@Composable
fun NearDisabledTextFieldPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearTextField(
            modifier =
                Modifier
                    .wrapContentHeight(),
            enabled = false,
            value = "비활성화",
            onValueChange = {},
            placeHolderText = "플레이스 홀더",
        )
    }
}

@Preview(widthDp = 370, heightDp = 80, showBackground = true)
@Composable
fun NearEnabledTextFieldPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearTextField(
            modifier =
                Modifier
                    .wrapContentHeight(),
            enabled = true,
            value = "입력완료",
            onValueChange = {},
            placeHolderText = "플레이스 홀더",
        )
    }
}
