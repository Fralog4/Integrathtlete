package com.app.integrathlete.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.integrathlete.R
import com.app.integrathlete.model.UserProfileViewModel

@Composable
fun ProfileSetupPage(
    onComplete: () -> Unit,
    sportsList: List<String> = emptyList()
) {
    val context = LocalContext.current
    val viewModel: UserProfileViewModel = hiltViewModel()

    // ... stati (sport, frequency, weight, etc.) rimangono uguali ...
    var sport by remember { mutableStateOf(sportsList.firstOrNull() ?: "") }
    var frequency by remember { mutableStateOf(1) }
    var weight by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("kg") }
    var height by remember { mutableStateOf("") }
    var heightUnit by remember { mutableStateOf("cm") }

    var expandedSport by remember { mutableStateOf(false) }
    var expandedFrequency by remember { mutableStateOf(false) }
    var expandedWeightUnit by remember { mutableStateOf(false) }
    var expandedHeightUnit by remember { mutableStateOf(false) }

    // ✅ Definiamo i colori personalizzati per i campi di testo (Blu)
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.primary, // Testo Blu quando scrivi
        unfocusedTextColor = MaterialTheme.colorScheme.primary, // Testo Blu quando non attivo
        focusedLabelColor = MaterialTheme.colorScheme.primary, // Label Blu quando attiva
        cursorColor = MaterialTheme.colorScheme.primary // Cursore Blu
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.onboarding_complete_profile),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface // Questo si adatta al tema (Bianco in dark, Nero in light)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Sport Dropdown ---
        Box {
            OutlinedTextField(
                value = sport,
                onValueChange = {},
                label = { Text(stringResource(R.string.profile_sport)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                colors = textFieldColors, // ✅ Applica colori blu
                trailingIcon = {
                    IconButton(onClick = { expandedSport = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
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

        // --- Frequenza Dropdown ---
        Box {
            OutlinedTextField(
                value = frequency.toString(),
                onValueChange = {},
                label = { Text(stringResource(R.string.profile_frequency)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                colors = textFieldColors, // ✅ Applica colori blu
                trailingIcon = {
                    IconButton(onClick = { expandedFrequency = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
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

        // --- Peso ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = weight,
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) weight = it },
                label = { Text(stringResource(R.string.profile_weight)) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors // ✅ Applica colori blu
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                OutlinedTextField(
                    value = weightUnit,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.profile_unit)) },
                    readOnly = true,
                    modifier = Modifier.width(100.dp),
                    colors = textFieldColors, // ✅ Applica colori blu
                    trailingIcon = {
                        IconButton(onClick = { expandedWeightUnit = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedWeightUnit,
                    onDismissRequest = { expandedWeightUnit = false }
                ) {
                    listOf("kg", "lbs").forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                weightUnit = unit
                                expandedWeightUnit = false
                                // Logica conversione peso rimossa per brevità, mantienila se c'era
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Altezza ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = height,
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) height = it },
                label = { Text(stringResource(R.string.profile_height)) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors // ✅ Applica colori blu
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                OutlinedTextField(
                    value = heightUnit,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.profile_unit)) },
                    readOnly = true,
                    modifier = Modifier.width(100.dp),
                    colors = textFieldColors, // ✅ Applica colori blu
                    trailingIcon = {
                        IconButton(onClick = { expandedHeightUnit = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedHeightUnit,
                    onDismissRequest = { expandedHeightUnit = false }
                ) {
                    listOf("cm", "in").forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                heightUnit = unit
                                expandedHeightUnit = false
                                // Logica conversione altezza rimossa per brevità
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val w = weight.toFloatOrNull()
                val h = height.toIntOrNull()
                if (sport.isNotBlank() && w != null && h != null) {
                    // Logica salvataggio
                    val weightInKg = if (weightUnit == "lbs") w / 2.20462f else w
                    val heightInCm = if (heightUnit == "in") (h * 2.54).toInt() else h

                    viewModel.updateUserPreferences(sport, frequency, weightInKg, heightInCm)
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.onboarding_save_continue))
        }
    }
}