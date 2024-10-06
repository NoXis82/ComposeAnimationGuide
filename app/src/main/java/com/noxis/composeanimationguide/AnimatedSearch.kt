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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier
        ) {
            drawArc(
                color = Color(0xFF302522),
                startAngle = 45f,
                sweepAngle = 360f * animateCircle.value,
                useCenter = false,
                size = Size(80f, 80f),
                style = Stroke(16f, cap = StrokeCap.Round)
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
