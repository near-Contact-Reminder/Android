package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.textfield.internal.NearOutlinedTextFieldDecorationBox
import com.alarmy.near.presentation.ui.component.textfield.internal.NearTextFieldColors
import com.alarmy.near.presentation.ui.component.textfield.internal.NearTextFieldDecorationContainer
import com.alarmy.near.presentation.ui.theme.NearTheme

private const val MAX_TEXT_COUNT = 200

@Composable
fun NearLimitedTextField(
    modifier: Modifier = Modifier,
    value: String,
    maxTextCount: Int = MAX_TEXT_COUNT,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    placeHolderText: String,
    singleLine: Boolean = false,
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
    val colors = NearTextFieldColors()

    // TextCount에서 End,Bottom에 Margin을 주고자 했는데, 원인은 모르곘으나 마진차가 있었음
    // 이를 해결하기 위한 임시 해결책으로 1줄일 때 가운데 정렬 설정
    val lineCount = remember { mutableIntStateOf(1) }

    NearTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        placeHolderText = placeHolderText,
        onTextLayout = {
            lineCount.intValue = it.lineCount
        },
        singleLine = singleLine,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            NearOutlinedTextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                interactionSource = interactionSource,
                colors = colors,
                placeHolderText = placeHolderText,
                contentPadding =
                    PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 76.dp,
                        // Container 구조상 Row를 통해 TextCount의 텍스트와 마진을 줄 수 없는 구조
                        // 현재 차선책으로 강제 value 설정
                    ),
                container = {
                    NearTextFieldDecorationContainer(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        colors = colors,
                    )
                    if (value.count() > 0 && enabled) {
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment =
                                if (lineCount.intValue == 1) {
                                    Alignment.CenterVertically
                                } else {
                                    Alignment.Bottom
                                },
                        ) {
                            Text(
                                text = "${value.count()}/$maxTextCount",
                                modifier =
                                    Modifier.padding(
                                        end = 16.dp,
                                        bottom = if (lineCount.intValue == 1) 0.dp else 16.dp,
                                    ),
                                style = NearTheme.typography.B2_14_MEDIUM,
                                color = NearTheme.colors.GRAY02_B7B7B7,
                            )
                        }
                    }
                },
            )
        },
    )
}

@Preview(widthDp = 370, heightDp = 52, showBackground = true)
@Composable
fun NearEnabledLimitedTextFieldPreview() {
    Surface(modifier = Modifier.padding(horizontal = 20.dp)) {
        NearLimitedTextField(
            modifier =
                Modifier
                    .wrapContentHeight(),
            enabled = true,
            value = "테스트",
            onValueChange = {},
            placeHolderText = "플레이스 홀더",
        )
    }
}
