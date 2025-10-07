package com.alarmy.near.presentation.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.button.NearLineTypeButton
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearBasicDialog(
    onDismiss: () -> Unit,
    title: String? = null,
    body: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissButtonClick: (() -> Unit),
    onConfirm: (() -> Unit),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title =
            title?.let { dialogTitle ->
                {
                    Text(
                        text = dialogTitle,
                        style = NearTheme.typography.H2_18_BOLD,
                        color = NearTheme.colors.BLACK_1A1A1A,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        text = {
            Text(
                text = body,
                style = NearTheme.typography.B1_16_MEDIUM,
                color = NearTheme.colors.BLACK_1A1A1A,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                NearLineTypeButton(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    enabled = true,
                    text = dismissButtonText,
                    onClick = onDismissButtonClick,
                )

                NearBasicButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Text(
                        text = confirmButtonText,
                        style = NearTheme.typography.B1_16_BOLD,
                    )
                }
            }
        },
        containerColor = NearTheme.colors.WHITE_FFFFFF,
        shape = RoundedCornerShape(16.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun NearBasicDialogPreview() {
    NearTheme {
        NearBasicDialog(
            onDismiss = {},
            title = "권한이 필요합니다",
            body = "연락처 접근 권한이 필요합니다.\n설정에서 권한을 허용해주세요.",
            dismissButtonText = "취소",
            confirmButtonText = "설정으로 이동",
            onDismissButtonClick = {},
            onConfirm = {},
        )
    }
}
