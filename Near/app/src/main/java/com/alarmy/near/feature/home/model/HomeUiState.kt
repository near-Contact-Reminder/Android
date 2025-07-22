package com.alarmy.near.feature.home.model

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val data: Any,
    ) : HomeUiState
}
