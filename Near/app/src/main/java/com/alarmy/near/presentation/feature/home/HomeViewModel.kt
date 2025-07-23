package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.ExampleRepository
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val exampleRepository: ExampleRepository,
    ) : ViewModel() {
        private val _uiStateFlow = MutableStateFlow(HomeUiState.Loading)
        val uiStateFlow = _uiStateFlow.asStateFlow()

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
