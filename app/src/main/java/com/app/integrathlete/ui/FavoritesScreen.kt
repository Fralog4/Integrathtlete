package com.app.integrathlete.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.integrathlete.model.Supplement

@Composable
fun FavoritesScreen(
    favoriteSupplements: List<Supplement>,
    onSupplementClick: (Supplement) -> Unit
) {
    if (favoriteSupplements.isEmpty()) {
        // Schermata vuota (mockup)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nessun integratore salvato tra i preferiti.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        // In futuro: usa la SupplementList riutilizzabile
        SupplementList(supplements = favoriteSupplements, onItemClick = onSupplementClick)
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(favoriteSupplements = emptyList(), onSupplementClick = {})
}
