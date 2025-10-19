package com.alarmy.near.data.provider

import android.content.Context

/**
 * Activity Context 제공 인터페이스
 * 카카오 로그인 등 Activity Context가 필요한 경우에 사용
 */
interface ActivityContextProvider {
    /**
     * 현재 Activity의 Context를 반환
     * Activity Context, 없으면 null
     */
    fun getActivityContext(): Context?
}

