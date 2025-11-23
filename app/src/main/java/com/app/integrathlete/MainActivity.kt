package com.app.integrathlete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.model.MainUiState
import com.app.integrathlete.model.MainViewModel
import com.app.integrathlete.model.UserProfileViewModel
import com.app.integrathlete.ui.MainScreenWithBottomNav
import com.app.integrathlete.ui.OnboardingPreferences
import com.app.integrathlete.ui.OnboardingScreen
import com.app.integrathlete.ui.profile.UserProfileScreen
import com.app.integrathlete.ui.theme.IntegrathleteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IntegrathleteTheme {
                val context = applicationContext
                val scope = rememberCoroutineScope()

                // 1. Inizializziamo il nuovo MainViewModel
                val mainViewModel: MainViewModel = viewModel()
                // Osserviamo lo stato del caricamento dati
                val mainUiState by mainViewModel.uiState.collectAsState()

                // ViewModel esistente per il profilo
                val userViewModel: UserProfileViewModel = hiltViewModel()

                // Gestione stato onboarding
                var onboardingDone by remember { mutableStateOf<Boolean?>(null) }
                var profileDone by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    onboardingDone = OnboardingPreferences.isCompleted(context)
                }

                // 2. Gestione della UI basata sullo stato dei dati
                when (val state = mainUiState) {
                    is MainUiState.Loading -> {
                        // Mostriamo una rotellina di caricamento al centro
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is MainUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message)
                        }
                    }
                    is MainUiState.Success -> {
                        // DATI PRONTI! Procediamo con la logica dell'app normale
                        val allSupplements = state.supplements
                        val sports = state.sports

                        // Qui inizia la logica originale dell'app
                        when (onboardingDone) {
                            null -> { /* Loading Preferences... invisibile o splash screen */ }

                            false -> {
                                OnboardingScreen(
                                    onFinish = {
                                        scope.launch {
                                            OnboardingPreferences.setCompleted(context, true)
                                            onboardingDone = true
                                        }
                                    },
                                    sportsList = sports, // Passiamo i dati caricati dal ViewModel
                                    viewModel = userViewModel
                                )
                            }

                            true -> {
                                if (!profileDone) {
                                    UserProfileScreen(
                                        onFinish = { profileDone = true }
                                    )
                                } else {
                                    MainScreenWithBottomNav(
                                        allSupplements = allSupplements // Passiamo i dati caricati
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}