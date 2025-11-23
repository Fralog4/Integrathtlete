package com.app.integrathlete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel // Assicurati che questo import ci sia
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

                // 1. Inizializziamo entrambi i ViewModel con Hilt
                val mainViewModel: MainViewModel = hiltViewModel()
                val userViewModel: UserProfileViewModel = hiltViewModel()

                // 2. Osserviamo lo stato del caricamento dati dal MainViewModel
                val mainUiState by mainViewModel.uiState.collectAsState()

                // Gestione stato onboarding
                var onboardingDone by remember { mutableStateOf<Boolean?>(null) }
                var profileDone by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    onboardingDone = OnboardingPreferences.isCompleted(context)
                }

                // 3. Gestione della UI basata sullo stato dei dati
                when (val state = mainUiState) {
                    is MainUiState.Loading -> {
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
                        val allSupplements = state.supplements
                        val sports = state.sports

                        when (onboardingDone) {
                            null -> { /* Loading Preferences... */ }

                            false -> {
                                OnboardingScreen(
                                    onFinish = {
                                        scope.launch {
                                            OnboardingPreferences.setCompleted(context, true)
                                            onboardingDone = true
                                        }
                                    },
                                    sportsList = sports,
                                    viewModel = userViewModel
                                )
                            }

                            true -> {
                                if (!profileDone) {
                                    // UserProfileScreen recupererà il suo ViewModel autonomamente con hiltViewModel()
                                    // oppure puoi passargli userViewModel se la screen lo accetta come parametro.
                                    // Qui assumiamo che la screen si arrangi o usi quello passato:
                                    UserProfileScreen(
                                        onFinish = { profileDone = true }
                                    )
                                } else {
                                    MainScreenWithBottomNav(
                                        allSupplements = allSupplements
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