package com.alarmy.near.presentation.provider

import android.content.Context
import com.alarmy.near.data.provider.ActivityContextProvider
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Activity Context 제공 구현체
 * MainActivity에서 직접 Context를 설정하는 방식
 */
@Singleton
class ActivityContextProviderImpl
    @Inject
    constructor() : ActivityContextProvider {
        private var activityContextRef: WeakReference<Context>? = null

        /**
         * Activity Context 설정
         * MainActivity의 onCreate/onDestroy에서 호출됨
         */
        fun setActivityContext(context: Context?) {
            activityContextRef =
                if (context != null) {
                    WeakReference(context)
                } else {
                    null
                }
        }

        override fun getActivityContext(): Context? = activityContextRef?.get()
    }
