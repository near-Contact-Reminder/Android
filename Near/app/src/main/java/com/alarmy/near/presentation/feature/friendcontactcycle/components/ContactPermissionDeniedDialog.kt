package com.alarmy.near.presentation.feature.friendcontactcycle.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.dialog.NearBasicDialog
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * 연락처 권한 거부 시 표시되는 다이얼로그
 */
@Composable
fun ContactPermissionDeniedDialog(
    onDismiss: () -> Unit,
    onGoToSettings: () -> Unit,
) {
    NearBasicDialog(
        onDismiss = onDismiss,
        title = stringResource(R.string.contact_permission_denied_title),
        body = stringResource(R.string.contact_permission_denied_message),
        dismissButtonText = stringResource(R.string.contact_permission_cancel),
        confirmButtonText = stringResource(R.string.contact_permission_go_to_settings),
        onDismissButtonClick = onDismiss,
        onConfirm = onGoToSettings,
    )
}

@Preview(showBackground = true)
@Composable
fun ContactPermissionDeniedDialogPreview() {
    NearTheme {
        ContactPermissionDeniedDialog(
            onDismiss = {},
            onGoToSettings = {},
        )
    }
}
