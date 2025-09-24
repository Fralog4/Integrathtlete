package com.app.integrathlete.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    sport: String,
    frequency: Int,
    weightKg: Float,
    heightCm: Int,
    onEditClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Il tuo profilo", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sport: $sport")
        Text("Frequenza: $frequency giorni/settimana")
        Text("Peso: ${"%.1f".format(weightKg)} kg")
        Text("Altezza: ${heightCm} cm")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onEditClick) {
            Text("Modifica profilo")
        }
    }
}
