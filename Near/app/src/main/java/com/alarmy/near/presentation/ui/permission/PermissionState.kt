package com.alarmy.near.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

enum class PermissionState {
    GRANTED,
    DENIED,
    SHOW_RATIONALE,
}

@Composable
fun rememberContactPermissionState(): PermissionState {
    val context = LocalContext.current
    var permissionState by remember { mutableStateOf(PermissionState.DENIED) }

    LaunchedEffect(Unit) {
        permissionState =
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS,
                ) == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED

                (context as? androidx.activity.ComponentActivity)?.shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS,
                ) == true -> PermissionState.SHOW_RATIONALE

                else -> PermissionState.DENIED
            }
    }

    return permissionState
}
