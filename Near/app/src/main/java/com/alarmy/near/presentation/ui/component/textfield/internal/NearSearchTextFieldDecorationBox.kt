package com.alarmy.near.presentation.ui.component.textfield.internal

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NearSearchTextFieldDecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    enabled: Boolean,
    singleLine: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
    placeHolderText: String,
    onSearchClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        top = 16.dp,
        bottom = 16.dp,
        end = 8.dp
    ),
) {
    OutlinedTextFieldDefaults.DecorationBox(
        contentPadding = contentPadding,
        value = value,
        innerTextField = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 40.dp)
                ) {
                    innerTextField()
                }

                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp),
                    enabled = enabled
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_24_search),
                        contentDescription = "검색",
                        modifier = Modifier.size(24.dp),
                        tint = NearTheme.colors.GRAY01_888888,
                    )
                }
            }
        },
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
            NearTextFieldDecorationContainer(
                enabled = enabled,
                interactionSource = interactionSource,
                colors = colors,
            )
        },
    )
}
