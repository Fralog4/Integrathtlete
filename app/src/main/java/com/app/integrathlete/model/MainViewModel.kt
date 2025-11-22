package com.app.integrathlete.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.integrathlete.loader.JsonHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Definiamo gli stati possibili della nostra UI
sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(
        val supplements: List<Supplement>,
        val sports: List<String>
    ) : MainUiState

    data class Error(val message: String) : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Spostiamo l'esecuzione su un thread di I/O (Input/Output)
                // così il thread principale (UI) rimane libero e fluido.
                val (supplements, sports) = withContext(Dispatchers.IO) {
                    val context = getApplication<Application>()
                    // Carichiamo i dati in parallelo o sequenzialmente (qui sequenziale è ok per ora)
                    val loadedSupplements = JsonHelper.loadSupplements(context)
                    val loadedSports = JsonHelper.loadSports(context)
                    Pair(loadedSupplements, loadedSports)
                }

                // Torniamo sul Main Thread per aggiornare lo stato
                _uiState.value = MainUiState.Success(supplements, sports)

            } catch (e: Exception) {
                _uiState.value = MainUiState.Error("Errore nel caricamento dati: ${e.message}")
            }
        }
    }
}