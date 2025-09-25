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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.login.components.NearBottomSheetDragHandle
import com.alarmy.near.presentation.feature.login.components.TermsAgreementItem
import com.alarmy.near.presentation.feature.login.model.TermType
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyConsentBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConsentComplete: () -> Unit,
    viewModel: LoginViewModel,
) {
    val termsAgreementState by viewModel.termsAgreementState.collectAsStateWithLifecycle()
    if (isVisible) {
        val bottomSheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            dragHandle = { NearBottomSheetDragHandle() },
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
                            ).onNoRippleClick { viewModel.toggleAllTermsAgreement() }
                            .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 전체 체크
                    TermsAllCheckAgreementSection(
                        termsAgreementState = termsAgreementState,
                        onToggleAllTerms = { viewModel.toggleAllTermsAgreement() },
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                // 개별 약관 동의
                TermsAgreementSection(
                    termsAgreementState = termsAgreementState,
                    onToggleIndividualTerms = { termType -> viewModel.toggleIndividualTermsAgreement(termType) },
                    onShowTermsDetail = { termType -> viewModel.showTermsDetail(termType) },
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
    val termsList =
        listOf(
            Triple(
                "${stringResource(R.string.privacy_consent_required_prefix)} ${stringResource(TermType.SERVICE_TERMS.titleRes)}",
                TermType.SERVICE_TERMS,
                termsAgreementState.isServiceTermsAgreed,
            ),
            Triple(
                "${stringResource(R.string.privacy_consent_required_prefix)} ${stringResource(TermType.PRIVACY_COLLECTION.titleRes)}",
                TermType.PRIVACY_COLLECTION,
                termsAgreementState.isPrivacyCollectionAgreed,
            ),
            Triple(
                "${stringResource(R.string.privacy_consent_required_prefix)} ${stringResource(TermType.PRIVACY_POLICY.titleRes)}",
                TermType.PRIVACY_POLICY,
                termsAgreementState.isPrivacyPolicyAgreed,
            ),
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
        termsList.forEachIndexed { index, (text, termType, isChecked) ->
            TermsAgreementItem(
                text = text,
                isChecked = isChecked,
                onCheckedChange = { onToggleIndividualTerms(termType) },
                onclick = { onShowTermsDetail(termType) },
                showDivider = index < termsList.size - 1,
            )
        }
    }
}

// Preview는 ViewModel이 필요하므로 제거하거나 다른 방식으로 구현
