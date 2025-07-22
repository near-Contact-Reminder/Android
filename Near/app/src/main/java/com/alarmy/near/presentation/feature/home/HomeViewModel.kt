package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.ViewModel
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiStateFlow = MutableStateFlow(HomeUiState.Loading)
        val uiStateFlow = _uiStateFlow.asStateFlow()

        fun fetchContacts() {
        }

        fun removeContact(id: Long) {
            // contactRepository.removeContact(id)
        }
    }
