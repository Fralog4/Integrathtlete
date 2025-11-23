package com.app.integrathlete.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.integrathlete.model.Form
import com.app.integrathlete.model.Product
import com.app.integrathlete.model.Study
import com.app.integrathlete.model.Supplement

@Composable
fun SupplementList(
    supplements: List<Supplement>,
    onItemClick: (Supplement) -> Unit
) {
    LazyColumn {
        items(supplements) { supplement ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp), // Elevation ridotta un po' per look più moderno
                // ✅ ORA USIAMO IL TEMA: surface (che abbiamo impostato a FAFAFA in light mode)
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = { onItemClick(supplement) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = "Supplement Icon",
                        // ✅ Colore primario del tema (BrandBlue)
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 16.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = supplement.name,
                            style = MaterialTheme.typography.titleLarge,
                            // ✅ Colore onSurface (Testo scuro/chiaro automatico)
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val bestFormsText = buildAnnotatedString {
                            append("Best forms: ")
                            supplement.forms.filter { it.quality == "high" }
                                .forEachIndexed { index, form ->
                                    // ✅ Colore primario anche qui per coerenza
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                        append(form.name)
                                    }
                                    if (index != supplement.forms.filter { it.quality == "high" }.lastIndex) {
                                        append(", ")
                                    }
                                }
                        }

                        Text(
                            text = bestFormsText,
                            style = MaterialTheme.typography.bodySmall,
                            // ✅ Colore onSurfaceVariant (Grigio medio)
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // FlowRow richiede Accompanist o Compose Foundation recente.
                        // Se FlowRow ti dà errore, usa Row o aggiungi la dipendenza corretta.
                        // Per ora assumiamo funzioni (se è nell'ultima Compose BOM).
                        @OptIn(ExperimentalLayoutApi::class)
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
                                            // ✅ Testo che si adatta allo sfondo variant
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        // ✅ Sfondo chip grigio chiaro
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                        Text(
                            text = supplement.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, // Grigio
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ... Preview rimane uguale ...