package com.alarmy.near.presentation.feature.friendprofileedittor.uistate

import com.alarmy.near.model.Anniversary
import com.alarmy.near.model.ContactFrequency
import com.alarmy.near.model.Friend
import com.alarmy.near.model.Relation

data class FriendProfileEditorUIState(
    val name: InputField<String> = InputField(""),
    val relation: Relation = Relation.FRIEND,
    val contactFrequency: ContactFrequency,
    val birthday: InputField<String?> = InputField(null),
    val anniversaries: List<AnniversaryUIState> = emptyList(),
    val memo: InputField<String?> = InputField(null),
)

data class AnniversaryUIState(
    val title: InputField<String> = InputField(""),
    val date: InputField<String?> = InputField(null),
)

data class InputField<T>(
    val value: T,
    val error: Boolean = false, // null이면 유효한 상태
    val isDirty: Boolean = false, // 유저가 입력을 시도했는지
)

fun Friend.toUiModel(): FriendProfileEditorUIState =
    FriendProfileEditorUIState(
        name = InputField(name),
        relation = relation,
        contactFrequency =
            contactFrequency.copy(
                dayOfWeek =
                    when (contactFrequency.dayOfWeek) {
                        "MONDAY" -> "월요일"
                        "TUESDAY" -> "화요일"
                        "WEDNESDAY" -> "수요일"
                        "THURSDAY" -> "목요일"
                        "FRIDAY" -> "금요일"
                        "SATURDAY" -> "토요일"
                        "SUNDAY" -> "일요일"
                        else -> IllegalStateException("없는 타입 입니다.")
                    } as String,
            ),
        birthday = InputField(birthday),
        anniversaries = anniversaryList.map { it.toUiModel() },
        memo = InputField(memo),
    )

fun Anniversary.toUiModel(): AnniversaryUIState =
    AnniversaryUIState(
        title = InputField(title),
        date = InputField(date),
    )
