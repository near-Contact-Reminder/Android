package com.alarmy.near.presentation.feature.myprofile.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.dialog.NearOutlinedDialog
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun WithdrawConfirmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    NearOutlinedDialog(
        onDismiss = onDismissRequest,
        title = null,
        body = stringResource(R.string.withdraw_confirm_content),
        dismissButtonText = stringResource(R.string.withdraw_confirm_cancel),
        confirmButtonText = stringResource(R.string.withdraw_confirm_withdraw),
        onDismissButtonClick = onDismissRequest,
        onConfirmButtonClick = onConfirm,
    )
}

@Preview(showBackground = true)
@Composable
fun WithdrawConfirmDialogPreview() {
    NearTheme {
        WithdrawConfirmDialog(
            onDismissRequest = { },
            onConfirm = {},
        )
    }
}
