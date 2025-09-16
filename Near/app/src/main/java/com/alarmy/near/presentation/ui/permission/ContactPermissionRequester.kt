package com.alarmy.near.presentation.ui.permission

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.alarmy.near.permission.PermissionState
import com.alarmy.near.permission.rememberContactPermissionState

@Composable
fun ContactPermissionRequester(
    onGranted: @Composable () -> Unit,
    onDenied: @Composable (onRequestPermission: () -> Unit) -> Unit,
    onShowRationale: @Composable (onRequestPermission: () -> Unit) -> Unit = onDenied,
) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {},
        )

    val permissionState = rememberContactPermissionState()

    when (permissionState) {
        PermissionState.GRANTED -> onGranted()
        PermissionState.DENIED -> onDenied { launcher.launch(Manifest.permission.READ_CONTACTS) }
        PermissionState.SHOW_RATIONALE -> onShowRationale { launcher.launch(Manifest.permission.READ_CONTACTS) }
    }
}
