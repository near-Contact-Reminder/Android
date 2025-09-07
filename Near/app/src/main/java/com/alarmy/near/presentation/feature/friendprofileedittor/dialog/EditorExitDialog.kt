package com.alarmy.near.presentation.feature.friendprofileedittor.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
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
            Text(text = stringResource(R.string.editor_exit_title))
        },
        text = {
            Text(
                text =
                    stringResource(R.string.editor_exit_content),
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(stringResource(R.string.editor_exit_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(R.string.editor_exit_dismiss))
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
