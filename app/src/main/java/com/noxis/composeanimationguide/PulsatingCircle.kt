package com.noxis.composeanimationguide

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun PulsatingCircle() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val infiniteTransition = rememberInfiniteTransition(label = "")

        val scale by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawCircle(
                color = Color(0xFFffb59c),
                center = Offset(
                    x = canvasWidth / 2,
                    y = canvasHeight / 2
                ),
                radius = size.minDimension / 4f,
            )
        }

    }

}