package com.app.integrathlete.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.integrathlete.model.Supplement
import com.app.integrathlete.model.UserProfileViewModel
import com.app.integrathlete.repository.UserProfileViewModelFactory

@Composable
fun SupplementSuggestionsScreen(
    supplements: List<Supplement>
) {
    val context = LocalContext.current
    val viewModel: UserProfileViewModel = viewModel(factory = UserProfileViewModelFactory(context))
    val userPrefs by viewModel.userPrefs.collectAsState()
    val userSport = userPrefs.sport

    val suggestedSupplements = supplements.filter { it.sports.contains(userSport) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Integratori suggeriti per: $userSport",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (suggestedSupplements.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun integratore suggerito per lo sport \"$userSport\"")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(suggestedSupplements) { supplement ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = supplement.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = supplement.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
