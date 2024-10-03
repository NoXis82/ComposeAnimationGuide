package com.noxis.composeanimationguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noxis.composeanimationguide.ui.theme.ComposeAnimationGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAnimationGuideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        val transition = rememberInfiniteTransition(label = "")
                        val color by transition.animateColor(
                            initialValue = Color.Red,
                            targetValue = Color.Green,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "color"
                        )

                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .drawBehind {
                                    drawRect(color)
                                }
                        )

                    }
                }
            }
        }
    }
}
