package com.app.integrathlete.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.app.integrathlete.model.Product
import com.app.integrathlete.model.Supplement
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.model.SupplementViewModel

@Composable
fun SupplementDetailScreen(
    supplement: Supplement, modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val sortedProducts = supplement.sortedProducts()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = supplement.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = supplement.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Available Forms",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            supplement.forms.forEach { form ->
                Text(
                    text = "${form.name} (${form.quality})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Scientific Studies",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            supplement.studies.forEach { study ->
                Text(
                    text = study.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(study.link))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Consigliato per",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                supplement.sports.forEach { sport ->
                    AssistChip(
                        onClick = {},
                        label = { Text(text = sport, color = Color.Black) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE0E0E0),
                            labelColor = Color.Black
                        )
                    )
                }
            }

            Text(
                text = "Recommended Products",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(sortedProducts) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(2.dp)
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
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Price: €${product.price}",
                        style = MaterialTheme.typography.bodyMedium
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


