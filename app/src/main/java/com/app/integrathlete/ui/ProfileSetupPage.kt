package com.app.integrathlete.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.model.UserProfileViewModel
import com.app.integrathlete.repository.UserProfileViewModelFactory

@Composable
fun ProfileSetupPage(
    onComplete: () -> Unit,
    sportsList: List<String> = emptyList() // passiamo lista sport caricata dal JSON
) {
    val context = LocalContext.current
    val viewModel: UserProfileViewModel = viewModel(factory = UserProfileViewModelFactory(context))
    // Stati per i campi
    var sport by remember { mutableStateOf(sportsList.firstOrNull() ?: "") }
    var frequency by remember { mutableStateOf(1) }
    var weight by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("kg") } // kg o libbre
    var height by remember { mutableStateOf("") }
    var heightUnit by remember { mutableStateOf("cm") } // cm o inches

    // Dropdown visibili?
    var expandedSport by remember { mutableStateOf(false) }
    var expandedFrequency by remember { mutableStateOf(false) }
    var expandedWeightUnit by remember { mutableStateOf(false) }
    var expandedHeightUnit by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Completa il tuo profilo", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Sport Dropdown ---
        Box {
            OutlinedTextField(
                value = sport,
                onValueChange = {},
                label = { Text("Sport praticato") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedSport = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = expandedSport,
                onDismissRequest = { expandedSport = false }
            ) {
                sportsList.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s) },
                        onClick = {
                            sport = s
                            expandedSport = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Frequenza Dropdown (1-7) ---
        Box {
            OutlinedTextField(
                value = frequency.toString(),
                onValueChange = {},
                label = { Text("Frequenza (giorni/settimana)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedFrequency = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = expandedFrequency,
                onDismissRequest = { expandedFrequency = false }
            ) {
                (1..7).forEach { day ->
                    DropdownMenuItem(
                        text = { Text(day.toString()) },
                        onClick = {
                            frequency = day
                            expandedFrequency = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Peso con unità ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = weight,
                onValueChange = { newValue ->
                    // Accetta solo numeri e punto
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        weight = newValue
                    }
                },
                label = { Text("Peso") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                OutlinedTextField(
                    value = weightUnit,
                    onValueChange = {},
                    label = { Text("Unità") },
                    readOnly = true,
                    modifier = Modifier.width(100.dp),
                    trailingIcon = {
                        IconButton(onClick = { expandedWeightUnit = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedWeightUnit,
                    onDismissRequest = { expandedWeightUnit = false }
                ) {
                    listOf("kg", "libbre").forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                weightUnit = unit
                                expandedWeightUnit = false
                                // Optional: convert weight to new unit
                                weight = convertWeight(weight, weightUnit, unit)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Altezza con unità ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = height,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        height = newValue
                    }
                },
                label = { Text("Altezza") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                OutlinedTextField(
                    value = heightUnit,
                    onValueChange = {},
                    label = { Text("Unità") },
                    readOnly = true,
                    modifier = Modifier.width(100.dp),
                    trailingIcon = {
                        IconButton(onClick = { expandedHeightUnit = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedHeightUnit,
                    onDismissRequest = { expandedHeightUnit = false }
                ) {
                    listOf("cm", "inches").forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                heightUnit = unit
                                expandedHeightUnit = false
                                // Optional: convert height to new unit
                                height = convertHeight(height, heightUnit, unit)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Validazione semplice
                val w = weight.toFloatOrNull()
                val h = height.toIntOrNull()
                if (sport.isBlank() || w == null || h == null) {
                    // mostra messaggio errore o Toast
                    return@Button
                }

                // Converti peso e altezza in kg e cm (unità base)
                val weightInKg = if (weightUnit == "libbre") w / 2.20462f else w
                val heightInCm = if (heightUnit == "inches") (h * 2.54).toInt() else h

                // Salva tramite ViewModel
                viewModel.updateUserPreferences(
                    sport = sport,
                    frequency = frequency,
                    weightKg = weightInKg,
                    heightCm = heightInCm
                )

                // Chiama callback per chiudere onboarding
                onComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salva e continua")
        }
    }
}

// Funzione per conversione peso
fun convertWeight(weightStr: String, fromUnit: String, toUnit: String): String {
    val weight = weightStr.toFloatOrNull() ?: return ""
    return if (fromUnit == toUnit) {
        weightStr
    } else if (toUnit == "kg") {
        // libbre -> kg
        String.format("%.1f", weight / 2.20462f)
    } else {
        // kg -> libbre
        String.format("%.1f", weight * 2.20462f)
    }
}

// Funzione per conversione altezza
fun convertHeight(heightStr: String, fromUnit: String, toUnit: String): String {
    val height = heightStr.toFloatOrNull() ?: return ""
    return if (fromUnit == toUnit) {
        heightStr
    } else if (toUnit == "cm") {
        // inches -> cm
        String.format("%.0f", height * 2.54f)
    } else {
        // cm -> inches
        String.format("%.0f", height / 2.54f)
    }
}
