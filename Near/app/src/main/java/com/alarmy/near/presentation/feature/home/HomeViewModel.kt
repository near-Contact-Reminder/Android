package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.ExampleRepository
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val exampleRepository: ExampleRepository,
    ) : ViewModel() {
        // Example: 여러번 초기화되는 StateFlow
        private val _uiStateFlow = MutableStateFlow(HomeUiState.Loading)
        val uiStateFlow = _uiStateFlow.asStateFlow()

        // Example: 한 번만 초기화되는 StateFlow
        private val exampleStateFlow =
            exampleRepository
                .getData()
                .map {
                    // Mapping to UIState
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = HomeUiState.Loading,
                )

        fun fetchContacts() {
            viewModelScope.launch {
                exampleRepository
                    .getData()
                    .catch {
                        // handle error
                    }.collect {
                        // updateUI
                    }
            }
        }

        fun removeContact(id: Long) {
            // contactRepository.removeContact(id)
        }
    }
