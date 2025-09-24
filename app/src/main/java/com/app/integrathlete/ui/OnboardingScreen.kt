package com.app.integrathlete.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.integrathlete.model.UserProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: UserProfileViewModel,
    sportsList: List<String>
) {
    val pages = listOf(
        "Benvenuto su IntegrAthlete",
        "Scopri integratori per ogni sport",
        "Personalizza la tua esperienza",
        "Completa il tuo profilo"
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            when (page) {
                0, 1, 2 -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pages[page],
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                3 -> ProfileSetupPage(
                    onComplete = { onFinish() },
                    sportsList = sportsList
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (selected) 12.dp else 8.dp)
                        .background(
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (pagerState.currentPage < pages.lastIndex) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Avanti")
            }
        }
    }
}
