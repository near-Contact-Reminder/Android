package com.alarmy.near.presentation.feature.friendcontactcycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ContactCycleStep {
    LOAD_CONTACTS, // 연락처 불러오기 단계
    SET_CYCLE,     // 연락 주기 설정 단계
}

@HiltViewModel
class FriendContactViewModel
    @Inject
    constructor() : ViewModel() {
    
    private val _currentStep = MutableStateFlow(ContactCycleStep.LOAD_CONTACTS)
    val currentStep: StateFlow<ContactCycleStep> = _currentStep.asStateFlow()
    
    // 짱구 관련 더미데이터 생성
    val contacts = listOf<FriendContactUIModel>()
    
    fun moveToNextStep() {
        viewModelScope.launch {
            _currentStep.value = ContactCycleStep.SET_CYCLE
        }
    }
    
    fun moveToPreviousStep() {
        viewModelScope.launch {
            _currentStep.value = ContactCycleStep.LOAD_CONTACTS
        }
    }
}
