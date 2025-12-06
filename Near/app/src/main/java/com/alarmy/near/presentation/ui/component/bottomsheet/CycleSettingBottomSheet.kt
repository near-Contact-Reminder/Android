package com.alarmy.near.presentation.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendcontactcycle.components.ContactCycleButtons
import com.alarmy.near.presentation.ui.component.checkbox.NearCheckbox
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import com.alarmy.near.utils.extensions.DateExtension

@Composable
fun CycleSettingBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onComplete: (ReminderInterval) -> Unit = {},
    currentSelectedInterval: ReminderInterval? = null,
    modifier: Modifier = Modifier,
) {
    var selectedInterval by remember(isVisible) {
        mutableStateOf<ReminderInterval?>(currentSelectedInterval ?: ReminderInterval.EVERY_WEEK)
    }

    NearBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.friend_contact_cycle_cycle_setting_text),
            style = NearTheme.typography.B1_16_BOLD,
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NearTheme.colors.BG02_F4F9FD)
                    .padding(vertical = 18.dp, horizontal = 20.dp),
        ) {
            Text(
                text =
                    buildAnnotatedString {
                        val interval = selectedInterval ?: ReminderInterval.EVERY_WEEK
                        val cycleText = DateExtension.getCycleText(interval)
                        val firstSpaceIndex = cycleText.indexOf(' ')

                        if (firstSpaceIndex != -1) {
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = NearTheme.colors.BLACK_1A1A1A,
                                        fontWeight = NearTheme.typography.B2_14_MEDIUM.fontWeight,
                                    ),
                            ) {
                                append(cycleText.substring(0, firstSpaceIndex + 1))
                            }
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = NearTheme.colors.BLUE01_5AA2E9,
                                        fontWeight = NearTheme.typography.B2_14_BOLD.fontWeight,
                                    ),
                            ) {
                                append(cycleText.substring(firstSpaceIndex + 1))
                            }
                        } else {
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = NearTheme.colors.BLACK_1A1A1A,
                                        fontWeight = NearTheme.typography.B2_14_MEDIUM.fontWeight,
                                    ),
                            ) {
                                append(cycleText)
                            }
                        }
                    },
                style = NearTheme.typography.B2_14_MEDIUM,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(20.dp))

                VerticalDivider(
                    modifier =
                        Modifier
                            .size(width = 1.dp, height = 28.dp),
                    color =
                        NearTheme.colors.BLACK_1A1A1A.copy(
                            alpha = 0.1f,
                        ),
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text =
                        "${stringResource(R.string.friend_contact_cycle_next_cycle_prefix)} ${
                            selectedInterval?.let { DateExtension.getNextCycleDate(it) }
                                ?: DateExtension.getNextWeekSameDay()
                        }",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        ReminderInterval.entries.forEach { interval ->
            val isSelected = selectedInterval == interval

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onNoRippleClick {
                            if (!isSelected) {
                                selectedInterval = interval
                            }
                        }.padding(vertical = 15.dp),
            ) {
                Text(
                    text = stringResource(interval.labelRes),
                    style =
                        if (isSelected) {
                            NearTheme.typography.B1_16_BOLD
                        } else {
                            NearTheme.typography.B2_14_MEDIUM
                        },
                )

                if (isSelected) {
                    NearCheckbox(
                        checked = true,
                        onCheckedChange = {},
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(24.dp))

        ContactCycleButtons(
            onLeftButtonClick = onDismiss,
            onRightButtonClick = {
                selectedInterval?.let { interval ->
                    onComplete(interval)
                    onDismiss()
                }
            },
            leftButtonText = stringResource(R.string.friend_contact_cycle_cancel_button),
            rightButtonText = stringResource(R.string.friend_contact_cycle_complete_button),
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Preview
@Composable
fun CycleSettingBottomSheetPreview() {
    NearTheme {
        CycleSettingBottomSheet(
            isVisible = true,
            onDismiss = {},
        )
    }
}
