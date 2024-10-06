package com.noxis.composeanimationguide

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun AnimatedSearch() {

    // Simple progressive circle looking animation
    val animateCircle = remember { Animatable(0f) }.apply {
        AnimateShapeInfinitely(this)
    }

// 0.6f for initial value to reduce floating time of line to reach it's final state.
    // Settings it to 0f -> final animation output looks kind of aggressive movements.
    val animateLine = remember { Animatable(0.6f) }.apply {
        AnimateShapeInfinitely(this)
    }

    // Appears different for dark/light theme colors.
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Draw arc in canvas which forms animated circle, repeatable.
        Canvas(
            modifier = Modifier
        ) {
            drawArc(
                color = surfaceColor,
                startAngle = 45f,
                sweepAngle = 360f * animateCircle.value,
                useCenter = false,
                size = Size(80f, 80f),
                style = Stroke(16f, cap = StrokeCap.Round)
            )
            // Draw diagonal line in canvas.
            drawLine(
                color = surfaceColor,
                strokeWidth = 16f,
                cap = StrokeCap.Round,
                start = Offset(
                    animateLine.value * 80f,
                    animateLine.value * 80f
                ),
                end = Offset(
                    animateLine.value * 110f,
                    animateLine.value * 110f
                )
            )
        }
    }

}

@Composable
fun AnimateShapeInfinitely(
    // shape which will be animated infinitely.
    animatable: Animatable<Float, AnimationVector1D>,
    // final float state to be animated.
    targetValue: Float = 1f,
    // duration took for animating once.
    durationMillis: Int = 1000
) {
    LaunchedEffect(animatable) {
        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(delayMillis = durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
}
