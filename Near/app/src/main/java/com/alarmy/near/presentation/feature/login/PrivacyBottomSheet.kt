package com.alarmy.near.presentation.feature.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.feature.login.components.TermsAgreementItem
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyConsentBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConsentComplete: () -> Unit,
) {
    if (isVisible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = NearTheme.colors.GRAY03_EBEBEB,
                    width = 32.dp,
                    height = 6.dp,
                )
            },
            containerColor = NearTheme.colors.WHITE_FFFFFF,
            contentColor = NearTheme.colors.BLACK_1A1A1A,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 바텀시트 제목
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "서비스 약관 동의",
                    style = NearTheme.typography.B1_16_BOLD,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.size(24.dp))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = NearTheme.colors.GRAY03_EBEBEB,
                                shape = RoundedCornerShape(12.dp),
                            ).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NearBackgroundCheckbox(
                        checked = false,
                    ) { }

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "약관 전체 동의",
                        style = NearTheme.typography.B2_14_BOLD,
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                TermsAgreementSection()

                Spacer(modifier = Modifier.size(32.dp))

                NearBasicButton(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    onClick = onConsentComplete,
                ) {
                    Text(
                        text = "가입",
                        style = NearTheme.typography.B1_16_BOLD,
                    )
                }

                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun TermsAgreementSection() {
    val termsList =
        listOf(
            "[필수] 서비스 이용 약관",
            "[필수] 개인정보 수집 및 이용 동의서",
            "[필수] 개인정보 처리방침",
        )

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = NearTheme.colors.GRAY03_EBEBEB,
                    shape = RoundedCornerShape(12.dp),
                ).padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        termsList.forEachIndexed { index, term ->
            TermsAgreementItem(
                text = term,
                isChecked = false,
                onCheckedChange = { },
                showDivider = index < termsList.size - 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyConsentBottomSheetPreview() {
    NearTheme {
        PrivacyConsentBottomSheet(
            isVisible = true,
            onDismiss = { },
            onConsentComplete = { },
        )
    }
}
