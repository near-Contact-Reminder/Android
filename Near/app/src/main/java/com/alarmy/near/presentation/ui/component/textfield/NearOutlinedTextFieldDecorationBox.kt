package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearOutlinedTextFieldDecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    enabled: Boolean,
    singleLine: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
    placeHolderText: String,
) {
    OutlinedTextFieldDefaults.DecorationBox(
        contentPadding = PaddingValues(16.dp),
        value = value,
        innerTextField = innerTextField,
        enabled = enabled,
        singleLine = singleLine,
        interactionSource = interactionSource,
        visualTransformation = VisualTransformation.None,
        placeholder = {
            Text(
                text = placeHolderText,
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.GRAY02_B7B7B7,
            )
        },
        container = {
            OutlinedTextFieldDefaults.Container(
                enabled = enabled,
                isError = false,
                interactionSource = interactionSource,
                colors = colors,
                shape = RoundedCornerShape(12.dp),
                focusedBorderThickness = (1.5).dp,
                unfocusedBorderThickness = (1.5).dp,
            )
        },
    )
}
