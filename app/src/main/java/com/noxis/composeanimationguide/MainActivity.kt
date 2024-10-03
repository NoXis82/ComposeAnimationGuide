package com.noxis.composeanimationguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.noxis.composeanimationguide.ui.theme.ComposeAnimationGuideTheme

@OptIn(ExperimentalAnimationApi::class)
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

                        Button(onClick = { visible = !visible }) {
                            Text(text = "Toggle")
                        }

                        AnimatedContent(
                            targetState = visible,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            content = {
                                if (it) {
                                    Box(modifier = Modifier.background(Color.Green))
                                } else {
                                    Box(modifier = Modifier.background(Color.Red))
                                }
                            },
                            transitionSpec = {
//                                fadeIn() with fadeOut()
//                                slideInHorizontally {
//                                    -it
//                                } togetherWith fadeOut()

                                slideInHorizontally {
                                    if (visible) it else -it
                                } togetherWith slideOutHorizontally {
                                    if (visible) -it else it
                                }

                            },
                            label = ""
                        )
                    }
                }
            }
        }
    }
}
