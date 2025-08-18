package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.textfield.internal.NearOutlinedTextFieldDecorationBox
import com.alarmy.near.presentation.ui.component.textfield.internal.NearTextFieldColors
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
    onTextLayout: (textLayoutResult: TextLayoutResult) -> Unit = {},
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
    decorationBox: (@Composable (innerTextField: @Composable () -> Unit) -> Unit)? = null,
) {
    val colors = NearTextFieldColors()

    BasicTextField(
        value = value,
        modifier = modifier.heightIn(min = 0.dp, max = 52.dp),
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
        onTextLayout = onTextLayout
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
