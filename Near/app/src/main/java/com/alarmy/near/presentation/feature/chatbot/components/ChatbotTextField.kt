package com.alarmy.near.presentation.feature.chatbot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "near에게 물어보기",
    maxLines: Int = 4,
) {
    // 포커스 상태 관리
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor =
        if (isFocused || value.isNotEmpty()) {
            NearTheme.colors.BLUE02_8ACCFF
        } else {
            NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.1f)
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp),
                ).padding(horizontal = 16.dp, vertical = 12.dp)
                .background(NearTheme.colors.WHITE_FFFFFF),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 텍스트 입력 필드
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            textStyle =
                NearTheme.typography.B2_14_MEDIUM,
            maxLines = maxLines,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(NearTheme.colors.BLUE01_5AA2E9),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = NearTheme.typography.B2_14_MEDIUM,
                        color =
                            NearTheme.colors.BLACK_1A1A1A.copy(
                                alpha = 0.5f,
                            ),
                    )
                }
                innerTextField()
            },
        )

        // 전송 버튼
        IconButton(
            onClick = onSendClick,
            modifier =
                Modifier
                    .size(24.dp)
                    .align(Alignment.Bottom),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_24_arrow_chat_gray),
                contentDescription = "전송",
                tint =
                    if (value.isNotEmpty()) {
                        NearTheme.colors.BLUE01_5AA2E9
                    } else {
                        NearTheme.colors.GRAY02_B7B7B7
                    },
            )
        }
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
fun ChatbotTextFieldPreview() {
    NearTheme {
        ChatbotTextField(
            value = "",
            onValueChange = {},
            onSendClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
fun ChatbotTextFieldWithTextPreview() {
    NearTheme {
        ChatbotTextField(
            value = "안녕하세요",
            onValueChange = {},
            onSendClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
