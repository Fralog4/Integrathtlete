package com.app.integrathlete.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.integrathlete.UserPreferences
import com.app.integrathlete.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    // Stato esposto alla UI
    private val _userPrefs = MutableStateFlow(UserPreferences.getDefaultInstance())
    val userPrefs: StateFlow<UserPreferences> = _userPrefs.asStateFlow()

    init {
        // 🔄 Carica dati all'avvio
        viewModelScope.launch {
            repository.userPreferencesFlow
                .collect { prefs ->
                    _userPrefs.value = prefs
                }
        }
    }

    // ✍️ Metodo per aggiornare i dati
    fun updateUserPreferences(
        sport: String,
        frequency: Int,
        weightKg: Float,
        heightCm: Int
    ) {
        viewModelScope.launch {
            repository.updateUserPreferences(
                sport = sport,
                frequency = frequency,
                weightKg = weightKg,
                heightCm = heightCm
            )
        }
    }

    fun getSuggestedSupplements(
        supplements: List<Supplement>,
        sport: String
    ): List<Supplement> {
        return supplements.filter { supplement ->
            sport in supplement.sports //in futuro creeremo una relazione tra integratori e sport
        }
    }

}
