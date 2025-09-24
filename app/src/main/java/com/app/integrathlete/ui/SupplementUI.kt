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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.FlowRow
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
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                onClick = { onItemClick(supplement) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = "Supplement Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = supplement.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF212121)  // nero/antracite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Coloriamo le forme "best forms" in blu acceso
                        val bestFormsText = buildAnnotatedString {
                            append("Best forms: ")
                            supplement.forms.filter { it.quality == "high" }
                                .forEachIndexed { index, form ->
                                    withStyle(style = SpanStyle(color = Color(0xFF007AFF))) { // blu acceso
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
                            color = Color(0xFF666666)  // grigio medio per il resto del testo
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
                                        Text(text = sport, color = Color.Black)
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFFE0E0E0), // grigio chiaro
                                        labelColor = Color.Black
                                    )
                                )
                            }
                        }
                        Text(
                            text = supplement.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF444444),
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


@Preview(showBackground = true)
@Composable
fun SupplementListPreview() {
    val fakeSupplements = listOf(
        Supplement(
            id = 1,
            name = "Zinc",
            forms = listOf(
                Form("Zinc Picolinate", "high"),
                Form("Zinc Citrate", "high"),
                Form("Zinc Oxide", "low")
            ),
            sports = listOf("Wrestling", "Bodybuilding"),
            description = "Zinc is an essential mineral that supports immune function and performance.",
            studies = listOf(
                Study(
                    title = "Effect of zinc supplementation on wrestlers",
                    link = "https://pubmed.ncbi.nlm.nih.gov/16648789/"
                )
            ),
            products = listOf(
                Product("Now Foods", "Zinc Picolinate", 12.5, "https://example.com/product1"),
                Product("Solgar", "Zinc Citrate", 15.9, "https://example.com/product2"),
                Product("Generic", "Zinc Oxide", 5.0, "https://example.com/product3")
            )
        )
    )

    SupplementList(supplements = fakeSupplements, onItemClick = {})
}

fun filterSupplementsBySport(sport: String, allSupplements: List<Supplement>): List<Supplement> {
    return allSupplements.filter { it.sports.contains(sport) }
}

fun Supplement.sortedProducts(): List<Product> {
    return products.sortedWith(
        compareByDescending<Product> { product ->
            when (product.form) {
                in forms.filter { it.quality == "high" }.map { it.name } -> 2
                in forms.filter { it.quality == "medium" }.map { it.name } -> 1
                else -> 0
            }
        }.thenBy { it.price }
    )
}
