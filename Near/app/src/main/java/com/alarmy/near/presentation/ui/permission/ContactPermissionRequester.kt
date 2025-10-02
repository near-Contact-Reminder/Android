package com.alarmy.near.presentation.ui.permission

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.alarmy.near.permission.PermissionState
import com.alarmy.near.permission.rememberContactPermissionState

@Composable
fun ContactPermissionRequester(
    onGranted: () -> Unit,
    onDenied: @Composable (onRequestPermission: () -> Unit) -> Unit,
    onShowRationale: @Composable (onRequestPermission: () -> Unit) -> Unit = onDenied,
    onPermissionDenied: (() -> Unit)? = null, // 권한 거부 시 콜백 추가
) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                // 권한이 거부된 경우 콜백 호출
                onPermissionDenied?.invoke()
            }
        }

    val permissionState = rememberContactPermissionState()

    when (permissionState) {
        PermissionState.GRANTED -> onGranted()
        PermissionState.DENIED -> onDenied { launcher.launch(Manifest.permission.READ_CONTACTS) }
        PermissionState.SHOW_RATIONALE -> onShowRationale { launcher.launch(Manifest.permission.READ_CONTACTS) }
    }
}
