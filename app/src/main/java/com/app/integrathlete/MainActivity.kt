package com.app.integrathlete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.loader.JsonHelper
import com.app.integrathlete.model.UserProfileViewModel
import com.app.integrathlete.repository.UserProfileViewModelFactory
import com.app.integrathlete.ui.MainScreenWithBottomNav
import com.app.integrathlete.ui.OnboardingPreferences
import com.app.integrathlete.ui.OnboardingScreen
import com.app.integrathlete.ui.profile.UserProfileScreen
import com.app.integrathlete.ui.theme.IntegrathleteTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val allSupplements = JsonHelper.loadSupplements(this)
        val sports = JsonHelper.loadSports(this)

        enableEdgeToEdge()
        setContent {
            IntegrathleteTheme {
                val context = applicationContext
                val scope = rememberCoroutineScope()

                var onboardingDone by remember { mutableStateOf<Boolean?>(null) }
                var profileDone by remember { mutableStateOf(false) }

                // ✅ Crea ViewModel per onboarding/profile
                val viewModel: UserProfileViewModel = viewModel(
                    factory = UserProfileViewModelFactory(context)
                )

                // 🔄 Carica stato iniziale onboarding
                LaunchedEffect(Unit) {
                    onboardingDone = OnboardingPreferences.isCompleted(context)
                }

                when (onboardingDone) {
                    null -> {
                        // Loading UI se vuoi
                    }

                    false -> {
                        OnboardingScreen(
                            onFinish = {
                                scope.launch {
                                    OnboardingPreferences.setCompleted(context, true)
                                    onboardingDone = true
                                }
                            },
                            sportsList = sports,
                            viewModel = viewModel
                        )
                    }

                    true -> {
                        if (!profileDone) {
                            UserProfileScreen(
                                onFinish = { profileDone = true }
                            )
                        } else {
                            MainScreenWithBottomNav(
                                allSupplements = allSupplements
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        OnboardingPreferences.reset(context)
                                        onboardingDone = false
                                        profileDone = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Reset Onboarding")
                            }
                        }
                    }
                }
            }
        }
    }
}

