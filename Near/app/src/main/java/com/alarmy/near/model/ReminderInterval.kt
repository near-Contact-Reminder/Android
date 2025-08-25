package com.alarmy.near.model

import androidx.annotation.StringRes
import com.alarmy.near.R

enum class ReminderInterval(
    @param:StringRes val labelRes: Int,
) {
    EVERY_DAY(R.string.reminder_interval_daily),
    EVERY_WEEK(R.string.reminder_interval_weekly),
    EVERY_TWO_WEEK(R.string.reminder_interval_biweekly),
    EVERY_MONTH(R.string.reminder_interval_monthly),
    EVERY_SIX_MONTH(R.string.reminder_interval_semiannual),
}
