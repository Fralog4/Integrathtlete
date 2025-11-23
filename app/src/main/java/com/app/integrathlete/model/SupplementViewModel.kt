package com.app.integrathlete.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel // Import Hilt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // Import Inject

@HiltViewModel
class SupplementViewModel @Inject constructor() : ViewModel() { // Aggiunto @Inject constructor()

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