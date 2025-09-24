package com.app.integrathlete.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SportSelectionScreen(
    sports: List<String>,
    onSportSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Select your sport:",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF212121), // quasi nero
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sports) { sport ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                    onClick = { onSportSelected(sport) }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = sport,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF007AFF) // blu accento
                        )
                    }
                }
            }
        }
    }
}
