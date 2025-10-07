package com.alarmy.near.presentation.feature.friendprofileedittor.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.dialog.NearBasicDialog
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun EditorExitDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    NearBasicDialog(
        onDismiss = onDismissRequest,
        title = stringResource(R.string.editor_exit_title),
        body = stringResource(R.string.editor_exit_content),
        dismissButtonText = stringResource(R.string.editor_exit_dismiss),
        confirmButtonText = stringResource(R.string.editor_exit_confirm),
        onDismissButtonClick = onDismissRequest,
        onConfirm = onConfirm,
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
