package com.alarmy.near.presentation.feature.friendprofileedittor

import androidx.lifecycle.ViewModel
import com.alarmy.near.model.Relation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FriendProfileEditorViewModel
    @Inject
    constructor() : ViewModel() {
        private val _relation: MutableStateFlow<Relation?> = MutableStateFlow(null)
        val relation = _relation.asStateFlow()

        fun setRelation(relation: Relation) {
            _relation.update { relation }
        }
    }
