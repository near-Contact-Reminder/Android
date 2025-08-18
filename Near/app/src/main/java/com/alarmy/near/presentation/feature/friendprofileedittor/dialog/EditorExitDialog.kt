package com.alarmy.near.presentation.feature.friendprofileedittor.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun EditorExitDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "수정을 그만두시나요?")
        },
        text = {
            Text(
                text =
                    "화면을 나가면 \n" +
                        "수정 내용은 저장되지 않아요.",
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text("취소")
            }
        },
        shape = RoundedCornerShape(24.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun EditorExitDialogPreview() {
    NearTheme {
        EditorExitDialog(
            onDismissRequest = { },
            onConfirm = {},
        )
    }
}
