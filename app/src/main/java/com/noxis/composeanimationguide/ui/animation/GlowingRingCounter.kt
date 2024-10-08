package com.noxis.composeanimationguide.ui.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GlowingRingCounter(
    modifier: Modifier = Modifier,
    countTimer: Int = 3,
    colorStroke: Color = Color(0xfff9d71c)
) {

    var isAnimated by remember { mutableStateOf(false) }

    val count by animateIntAsState(
        targetValue = if (isAnimated) 0 else countTimer,
        label = "count",
        animationSpec = tween(durationMillis = countTimer * 1000, easing = LinearEasing)
    )

    val angle by animateFloatAsState(
        targetValue = if (isAnimated) 0f else -360f,
        label = "angle",
        animationSpec = tween(durationMillis = countTimer * 820, easing = LinearEasing)
    )

    LaunchedEffect(true) {
        isAnimated = !isAnimated
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .padding(24.dp)
        ) {

            AnimatedContent(
                modifier = Modifier.align(Alignment.Center),
                targetState = count,
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }, label = ""
            ) { count ->
                Text(
                    text = count.toString(),
                    fontSize = 36.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    softWrap = false
                )
            }
            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp), onDraw = {
                drawArc(
                    color = colorStroke,
                    style = Stroke(
                        width = 5f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    startAngle = 270f,
                    sweepAngle = angle,
                    useCenter = false
                )
            })
        }
    }
}