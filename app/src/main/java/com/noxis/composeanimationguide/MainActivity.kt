package com.noxis.composeanimationguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

                        var visible by remember {
                            mutableStateOf(false)
                        }

                        var isRound by remember {
                            mutableStateOf(false)
                        }

                        val transition = updateTransition(targetState = isRound, label = null)

                        val borderRadius by transition.animateInt(
                            transitionSpec = { tween(2000) },
                            label = "border",
                            targetValueByState = {
                                if (it) 100 else 0
                            })

                        val animatedAlpha by transition.animateFloat(
                            transitionSpec = { tween(1500) },
                            label = "alpha",
                            targetValueByState = {
                                if (it) 1.0f else 0f
                            }
                        )

                        Button(onClick = {
                            visible = !visible
                            isRound = !isRound
                        }) {
                            Text(text = "Toggle")
                        }

                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .graphicsLayer {
                                    alpha = animatedAlpha
                                }
                                .clip(RoundedCornerShape(borderRadius))
                                .background(Color.Green)
                        ) {

                        }
                    }
                }
            }
        }
    }
}
