package com.noxis.composeanimationguide

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCirclesAndSearch() {
    // Pulsating circles
    PulsatingCircle()
    // Search look-a-like animation
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 28.dp, end = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedSearch()
    }
}