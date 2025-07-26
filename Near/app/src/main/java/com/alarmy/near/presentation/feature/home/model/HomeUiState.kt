package com.alarmy.near.presentation.feature.home.model

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val data: Any,
    ) : HomeUiState
}
