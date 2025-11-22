package com.app.integrathlete.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.model.UserProfileViewModel

@Composable
fun UserProfileScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current

    // Factory manuale
    val viewModel: UserProfileViewModel = hiltViewModel()

    val userPrefs by viewModel.userPrefs.collectAsState()

    // Stato locale per i campi di input
    var sport by remember { mutableStateOf(userPrefs.sport) }
    var frequency by remember { mutableStateOf(userPrefs.frequency.toString()) }
    var weight by remember { mutableStateOf(userPrefs.weightKg.toString()) }
    var height by remember { mutableStateOf(userPrefs.heightCm.toString()) }

    LaunchedEffect(userPrefs) {
        sport = userPrefs.sport
        frequency = userPrefs.frequency.toString()
        weight = userPrefs.weightKg.toString()
        height = userPrefs.heightCm.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Profilo Utente",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = sport,
            onValueChange = { sport = it },
            label = { Text("Sport praticato") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = frequency,
            onValueChange = { frequency = it },
            label = { Text("Frequenza (giorni/settimana)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altezza (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val freq = frequency.toIntOrNull() ?: 0
                val w = weight.toFloatOrNull() ?: 0f
                val h = height.toIntOrNull() ?: 0

                viewModel.updateUserPreferences(sport, freq, w, h)
                onFinish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salva")
        }
    }
}