package com.alarmy.near.presentation.ui.component.textfield

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.textfield.internal.NearTextFieldColors
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 상수 정의
private val DEFAULT_MIN_HEIGHT = 56.dp
private val CHARACTER_COUNT_END_PADDING = 92.dp
private val ERROR_MESSAGE_START_PADDING = 16.dp
private val ERROR_MESSAGE_VERTICAL_PADDING = 6.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NearOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    placeholder: String = "",
    isError: Boolean = false,
    focusRequester: FocusRequester,
    maxLines: Int = Int.MAX_VALUE,
    maxLength: Int = 200,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: TextFieldColors = NearTextFieldColors(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    focusedBorderThickness: Float = 1.5f, // 포커스 시 border
    unfocusedBorderThickness: Float = 1f, // 포커스 해제 시 border
    showCharacterCount: Boolean = true, // 굴자수 보이기
) {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var hasEverBeenTyped by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        // 한 번이라도 텍스트가 입력되면 hasEverBeenTyped을 true로 설정
        if (value.isNotEmpty() && !hasEverBeenTyped) {
            hasEverBeenTyped = true
        }
    }

    // 에러 상태일 때 스크롤
    LaunchedEffect(isError) {
        coroutineScope.launch {
            delay(300)
            bringIntoViewRequester.bringIntoView()
        }
    }

    // 글자 수가 표시될 때 텍스트 영역을 위한 패딩 조정
    val adjustedContentPadding =
        remember(showCharacterCount, contentPadding) {
            if (!showCharacterCount) return@remember contentPadding

            PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + CHARACTER_COUNT_END_PADDING,
                bottom = contentPadding.calculateBottomPadding(),
            )
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length > maxLength) {
                        return@BasicTextField
                    }
                    onValueChange(newValue)
                },
                enabled = enabled,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = DEFAULT_MIN_HEIGHT)
                        .focusRequester(focusRequester),
                textStyle = NearTheme.typography.B2_14_MEDIUM,
                maxLines = maxLines,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = enabled,
                        singleLine = maxLines == 1,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        contentPadding = adjustedContentPadding,
                        placeholder = {
                            Text(
                                text = placeholder,
                                style = NearTheme.typography.B2_14_MEDIUM,
                                color = NearTheme.colors.GRAY02_B7B7B7,
                            )
                        },
                        colors = colors,
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = shape,
                                focusedBorderThickness = focusedBorderThickness.dp,
                                unfocusedBorderThickness = unfocusedBorderThickness.dp,
                            )
                        },
                    )
                },
            )

            // 글자수는 한 번이라도 입력된 후에는 계속 표시
            if (showCharacterCount && hasEverBeenTyped) {
                val characterCountPadding =
                    remember(contentPadding) {
                        Modifier.padding(
                            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = contentPadding.calculateBottomPadding(),
                        )
                    }

                CharacterCountText(
                    currentLength = value.length,
                    maxLength = maxLength,
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .wrapContentSize()
                            .then(characterCountPadding),
                )
            }
        }

        // 에러 메시지 표시
        if (isError) {
            Text(
                text = stringResource(R.string.textfield_error_message),
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.NEGATIVE_F04E4E,
                modifier =
                    Modifier
                        .padding(start = ERROR_MESSAGE_START_PADDING)
                        .padding(vertical = ERROR_MESSAGE_VERTICAL_PADDING),
            )
        }
    }
}

/**
 * 글자 수 표시 텍스트 컴포넌트
 */
@Composable
private fun CharacterCountText(
    modifier: Modifier = Modifier,
    currentLength: Int,
    maxLength: Int,
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.End,
        text = "$currentLength/$maxLength",
        style = NearTheme.typography.B2_14_MEDIUM,
        color = NearTheme.colors.GRAY02_B7B7B7,
    )
}

@Preview(name = "기본 상태", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_Default() {
    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = "기본 텍스트필드",
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "텍스트 입력됨", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_WithText() {
    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = "입력된 텍스트입니다",
                onValueChange = {},
                placeholder = "텍스트 입력",
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "비활성화 상태", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_Disabled() {
    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = "비활성화된 텍스트",
                onValueChange = {},
                placeholder = "비활성화",
                enabled = false,
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "멀티라인", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_Multiline() {
    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = "여러 줄에 걸쳐 입력된\n긴 텍스트입니다.\n이렇게 멀티라인으로\n표시됩니다.",
                onValueChange = {},
                placeholder = "여러 줄 텍스트 입력",
                maxLines = 4,
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "포커스 상태 (인터랙티브)", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_Interactive() {
    var text by remember { mutableStateOf("") }

    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "클릭해서 포커스 테스트",
                modifier = Modifier.fillMaxWidth(),
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "글자 수 표시", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_CharacterCount() {
    var text by remember { mutableStateOf("글자 수가 표시되는 텍스트필드입니다.") }

    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "글자 수 표시",
                showCharacterCount = true,
                maxLength = 50,
                modifier = Modifier.fillMaxWidth(),
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}

@Preview(name = "에러 상태", showBackground = true)
@Composable
fun NearOutlinedTextFieldPreview_Error() {
    NearTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            NearOutlinedTextField(
                value = "에러가 있는 텍스트",
                onValueChange = {},
                placeholder = "에러 상태 테스트",
                isError = true,
                modifier = Modifier.fillMaxWidth(),
                focusRequester = remember { FocusRequester() },
            )
        }
    }
}
