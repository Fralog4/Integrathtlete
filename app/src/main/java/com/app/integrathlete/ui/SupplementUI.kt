package com.app.integrathlete.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
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
        items(
            items = supplements
        ) { supplement : Supplement->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = { onItemClick(supplement) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = supplement.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = supplement.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
            sport = listOf("Wrestling", "Bodybuilding"),
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
