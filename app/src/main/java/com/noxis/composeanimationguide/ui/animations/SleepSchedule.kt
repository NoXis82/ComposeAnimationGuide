package com.noxis.composeanimationguide.ui.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noxis.composeanimationguide.ui.animations.components.TouchMoveControlTrack
import com.noxis.composeanimationguide.ui.animations.components.VerticalGroupTime
import com.noxis.composeanimationguide.ui.animations.components.textSecondary
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.toKotlinDuration

@Composable
fun SleepSchedule() {

    var startTime by remember {
        mutableStateOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0)))
    }

    var endTime by remember {
        mutableStateOf(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 0)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(45, 44, 46))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                VerticalGroupTime(isStart = true, startTime, endTime)
                VerticalGroupTime(isStart = false, startTime, endTime)
            }
            Spacer(modifier = Modifier.padding(28.dp))

            TouchMoveControlTrack(startTime, endTime, { time ->
                startTime = time
            }) { time ->
                endTime = time
            }

            Spacer(modifier = Modifier.padding(28.dp))

            Text(
                text = "${Duration.between(startTime, endTime).toKotlinDuration()}",
                fontSize = 20.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "This schedule meets your sleep goal.",
                color = textSecondary
            )

        }
    }


}