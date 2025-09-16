package com.alarmy.near.presentation.feature.myprofile.model

import androidx.annotation.StringRes
import com.alarmy.near.R

/**
 * 탈퇴 사유를 나타내는 enum
 */
enum class WithdrawReason(@StringRes val displayTextRes: Int) {
    REASON_DONT_USE_OFTEN(R.string.withdraw_reason_not_often),
    REASON_NEW_ACCOUNT(R.string.withdraw_reason_new_account),
    REASON_WORRIED_INFORMATION(R.string.withdraw_reason_worried_info),
    REASON_INCONVENIENT_SERVICE(R.string.withdraw_reason_inconvenient),
    REASON_OTHER(R.string.withdraw_reason_other),
}
