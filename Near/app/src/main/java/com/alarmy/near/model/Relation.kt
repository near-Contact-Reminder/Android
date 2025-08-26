package com.alarmy.near.model

import androidx.annotation.StringRes
import com.alarmy.near.R

enum class Relation(
    @param:StringRes val resId: Int,
) {
    FRIEND(R.string.relation_friend),
    FAMILY(R.string.relation_family),
    ACQUAINTANCE(R.string.relation_acquaintance),
}
