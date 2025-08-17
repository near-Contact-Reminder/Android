package com.alarmy.near.model

import androidx.annotation.StringRes
import com.alarmy.near.R

enum class ReminderInterval(
    @param:StringRes val labelRes: Int,
) {
    DAILY(R.string.reminder_interval_daily), // 매일
    WEEKLY(R.string.reminder_interval_weekly), // 매주
    BIWEEKLY(R.string.reminder_interval_biweekly), // 2주
    MONTHLY(R.string.reminder_interval_monthly), // 매달
    SEMIANNUAL(R.string.reminder_interval_semiannual), // 6개월
}
