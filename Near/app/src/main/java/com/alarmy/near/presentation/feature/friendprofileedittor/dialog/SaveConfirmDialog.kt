package com.alarmy.near.presentation.feature.friendprofileedittor.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.dialog.NearBasicDialog
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun SaveConfirmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    NearBasicDialog(
        onDismiss = onDismissRequest,
        body = stringResource(R.string.editor_save_confirm_content),
        dismissButtonText = stringResource(R.string.editor_save_confirm_cancel),
        confirmButtonText = stringResource(R.string.editor_save_confirm_save),
        onDismissButtonClick = onDismissRequest,
        onConfirmButtonClick = onConfirm,
    )
}

@Preview(showBackground = true)
@Composable
fun SaveConfirmDialogPreview() {
    NearTheme {
        SaveConfirmDialog(
            onDismissRequest = { },
            onConfirm = {},
        )
    }
}
