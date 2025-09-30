package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.textfield.internal.NearSearchTextFieldDecorationBox
import com.alarmy.near.presentation.ui.component.textfield.internal.NearTextFieldColors
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearSearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    placeHolderText: String = "",
    singleLine: Boolean = true,
    onTextLayout: (textLayoutResult: TextLayoutResult) -> Unit = {},
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
    val colors = NearTextFieldColors()

    NearTextField(
        value = value,
        modifier = modifier.heightIn(min = 0.dp, max = 52.dp),
        enabled = enabled,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
        singleLine = singleLine,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            NearSearchTextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                interactionSource = interactionSource,
                colors = colors,
                placeHolderText = placeHolderText,
                onSearchClick = onSearchClick,
            )
        }
    )
}

@Preview(widthDp = 370, heightDp = 80, showBackground = true)
@Composable
fun NearSearchTextFieldPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearSearchTextField(
            modifier = Modifier.wrapContentHeight(),
            value = "",
            onValueChange = {},
            onSearchClick = {},
            placeHolderText = "검색어를 입력하세요",
        )
    }
}

@Preview(widthDp = 370, heightDp = 80, showBackground = true)
@Composable
fun NearSearchTextFieldWithTextPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearSearchTextField(
            modifier = Modifier.wrapContentHeight(),
            value = "검색 텍스트",
            onValueChange = {},
            onSearchClick = {},
            placeHolderText = "검색어를 입력하세요",
        )
    }
}
