package com.alarmy.near

import android.app.Application
import com.alarmy.near.utils.PhoneNumberFormatter
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NearApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 카카오 SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        
        // PhoneNumberFormatter 초기화
        PhoneNumberFormatter.initialize(this)
    }
}
