package com.alarmy.near.presentation.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * 앱 설정 관련 유틸리티 함수들
 */
object AppSettingsUtil {

     // 앱의 애플리케이션 정보 페이지로 이동
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}
