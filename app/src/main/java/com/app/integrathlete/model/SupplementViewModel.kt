package com.app.integrathlete.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupplementViewModel : ViewModel() {

    private val _favorites = MutableStateFlow<Set<Supplement>>(emptySet())
    val favorites: StateFlow<Set<Supplement>> = _favorites

    fun toggleFavorite(supplement: Supplement) {
        viewModelScope.launch {
            _favorites.value = if (_favorites.value.contains(supplement)) {
                _favorites.value - supplement
            } else {
                _favorites.value + supplement
            }
        }
    }

    fun isFavorite(supplement: Supplement): Boolean {
        return _favorites.value.contains(supplement)
    }
}
