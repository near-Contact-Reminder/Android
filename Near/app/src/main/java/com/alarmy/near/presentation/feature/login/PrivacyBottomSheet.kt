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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.login.components.NearBottomSheetDragHandle
import com.alarmy.near.presentation.feature.login.components.TermsAgreementItem
import com.alarmy.near.presentation.feature.login.model.TermType
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyConsentBottomSheet(
    isVisible: Boolean,
    termsAgreementState: TermsAgreementState,
    onDismiss: () -> Unit,
    onConsentComplete: () -> Unit,
    onToggleAllTerms: () -> Unit,
    onToggleIndividualTerms: (TermType) -> Unit,
    onTermsClick: (TermType) -> Unit = {},
) {
    if (isVisible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
        val scope = rememberCoroutineScope()

        // 바텀시트를 닫고 약관 페이지로 이동
        val dismissAndNavigateToTerms = { termType: TermType ->
            scope.launch {
                bottomSheetState.hide()
                onTermsClick(termType)
            }
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            dragHandle = { NearBottomSheetDragHandle() },
            containerColor = NearTheme.colors.WHITE_FFFFFF,
            contentColor = NearTheme.colors.BLACK_1A1A1A,
        ) {
            PrivacyConsentBottomSheetContent(
                termsAgreementState = termsAgreementState,
                onToggleAllTerms = onToggleAllTerms,
                onToggleIndividualTerms = onToggleIndividualTerms,
                onConsentComplete = onConsentComplete,
                onShowTermsDetail = { termType ->
                    dismissAndNavigateToTerms(termType)
                },
            )
        }
    }
}

@Composable
private fun PrivacyConsentBottomSheetContent(
    termsAgreementState: TermsAgreementState,
    onToggleAllTerms: () -> Unit,
    onToggleIndividualTerms: (TermType) -> Unit,
    onConsentComplete: () -> Unit,
    onShowTermsDetail: (TermType) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 바텀시트 제목
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.privacy_consent_title),
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
                    ).onNoRippleClick { onToggleAllTerms() }
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 전체 체크
            TermsAllCheckAgreementSection(
                termsAgreementState = termsAgreementState,
                onToggleAllTerms = onToggleAllTerms,
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        // 개별 약관 동의
        TermsAgreementSection(
            termsAgreementState = termsAgreementState,
            onToggleIndividualTerms = onToggleIndividualTerms,
            onShowTermsDetail = onShowTermsDetail,
        )

        Spacer(modifier = Modifier.size(32.dp))

        NearBasicButton(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = onConsentComplete,
            enabled = termsAgreementState.isAllRequiredTermsAgreed,
        ) {
            Text(
                text = stringResource(R.string.privacy_consent_signup_button),
                style = NearTheme.typography.B1_16_BOLD,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun TermsAllCheckAgreementSection(
    termsAgreementState: TermsAgreementState,
    onToggleAllTerms: () -> Unit,
) {
    NearBackgroundCheckbox(
        checked = termsAgreementState.isAllAgreed,
        onCheckedChange = { onToggleAllTerms() },
    )

    Spacer(modifier = Modifier.size(8.dp))

    Text(
        text = stringResource(R.string.privacy_consent_all_agreement),
        style = NearTheme.typography.B2_14_BOLD,
    )
}

@Composable
private fun TermsAgreementSection(
    termsAgreementState: TermsAgreementState,
    onToggleIndividualTerms: (TermType) -> Unit,
    onShowTermsDetail: (TermType) -> Unit,
) {
    val requiredPrefix = stringResource(R.string.privacy_consent_required_prefix)

    // 약관 타입 목록을 상수로 정의하여 리컴포지션 시 재생성 방지
    val terms =
        remember {
            listOf(
                TermType.SERVICE_TERMS,
                TermType.PRIVACY_COLLECTION,
                TermType.PRIVACY_POLICY,
            )
        }

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
        terms.forEachIndexed { index, termType ->
            val isAgreed =
                when (termType) {
                    TermType.SERVICE_TERMS -> termsAgreementState.isServiceTermsAgreed
                    TermType.PRIVACY_COLLECTION -> termsAgreementState.isPrivacyCollectionAgreed
                    TermType.PRIVACY_POLICY -> termsAgreementState.isPrivacyPolicyAgreed
                }
            TermsAgreementItem(
                text = "$requiredPrefix ${stringResource(termType.titleRes)}",
                isChecked = isAgreed,
                onCheckedChange = { onToggleIndividualTerms(termType) },
                onclick = { onShowTermsDetail(termType) },
                showDivider = index < terms.size - 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrivacyConsentBottomSheetContentPreview() {
    NearTheme {
        PrivacyConsentBottomSheetContent(
            termsAgreementState =
                TermsAgreementState(
                    isAllAgreed = false,
                    isServiceTermsAgreed = true,
                    isPrivacyCollectionAgreed = false,
                    isPrivacyPolicyAgreed = true,
                ),
            onToggleAllTerms = {},
            onToggleIndividualTerms = {},
            onConsentComplete = {},
            onShowTermsDetail = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrivacyConsentBottomSheetAllAgreedContentPreview() {
    NearTheme {
        PrivacyConsentBottomSheetContent(
            termsAgreementState =
                TermsAgreementState(
                    isAllAgreed = true,
                    isServiceTermsAgreed = true,
                    isPrivacyCollectionAgreed = true,
                    isPrivacyPolicyAgreed = true,
                ),
            onToggleAllTerms = {},
            onToggleIndividualTerms = {},
            onConsentComplete = {},
            onShowTermsDetail = {},
        )
    }
}
