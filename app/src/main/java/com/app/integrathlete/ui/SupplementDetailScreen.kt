package com.app.integrathlete.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.app.integrathlete.R
import com.app.integrathlete.model.Product
import com.app.integrathlete.model.Supplement
import com.app.integrathlete.model.SupplementViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SupplementDetailScreen(
    supplement: Supplement,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Calcoliamo i prodotti ordinati usando la funzione locale definita in basso
    val sortedProducts = supplement.getSortedProducts()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = supplement.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = supplement.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.detail_available_forms), // "Forme Disponibili"
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            supplement.forms.forEach { form ->
                Text(
                    text = "${form.name} (${form.quality})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.detail_scientific_studies), // "Studi Scientifici"
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            supplement.studies.forEach { study ->
                Text(
                    text = study.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(study.link))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.detail_recommended_for), // "Consigliato per"
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                supplement.sports.forEach { sport ->
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = sport,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.detail_recommended_products), // "Prodotti Consigliati"
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(sortedProducts) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.link))
                            context.startActivity(intent)
                        }) {
                    Text(
                        text = "${product.brand} - ${product.form}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.detail_price, product.price), // Prezzo formattato
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplementDetailScreenWithTopBar(
    supplement: Supplement,
    onBackClick: () -> Unit,
    viewModel: SupplementViewModel
) {
    val favorites = viewModel.favorites.collectAsState().value
    val isFavorite = favorites.contains(supplement)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(supplement.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.toggleFavorite(supplement)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isFavorite) "Rimuovi dai preferiti" else "Aggiungi ai preferiti",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        SupplementDetailScreen(
            supplement = supplement,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// --- Funzione di utilità locale per ordinare i prodotti ---
// Ordina per qualità della forma (High > Medium > Low) e poi per prezzo crescente
private fun Supplement.getSortedProducts(): List<Product> {
    // Mappa delle forme ad alta qualità per un accesso veloce
    val highQualityForms = forms.filter { it.quality.equals("high", ignoreCase = true) }.map { it.name }
    val mediumQualityForms = forms.filter { it.quality.equals("medium", ignoreCase = true) }.map { it.name }

    return products.sortedWith(
        compareByDescending<Product> { product ->
            // Assegna un punteggio basato sulla qualità della forma del prodotto
            when (product.form) {
                in highQualityForms -> 2 // Priorità massima
                in mediumQualityForms -> 1 // Priorità media
                else -> 0 // Priorità bassa
            }
        }.thenBy { it.price } // A parità di qualità, vince il prezzo più basso
    )
}