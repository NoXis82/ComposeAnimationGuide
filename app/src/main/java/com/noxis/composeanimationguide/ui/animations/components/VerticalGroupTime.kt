package com.noxis.composeanimationguide.ui.animations.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noxis.composeanimationguide.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val textSecondary = Color(157, 156, 167)

@Composable
fun VerticalGroupTime(
    isStart: Boolean,
    startTime: LocalDateTime,
    endTime: LocalDateTime
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(top = 28.dp))

        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            SleepBedTimeIcon(isStart, Modifier.size(18.dp))
            Spacer(modifier = Modifier.padding(end = 6.dp))
            Text(
                text = if (isStart) "BEDTIME" else "WAKE UP",
                color = textSecondary
            )
        }
        Spacer(modifier = Modifier.padding(top = 2.dp))
        Text(
            text = if (isStart) startTime.format(DateTimeFormatter.ofPattern("hh:mm a")) else endTime.format(
                DateTimeFormatter.ofPattern("hh:mm a")
            ),
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        Text(
            text = if (isStart) "Today" else "Tomorrow",
            color = textSecondary
        )
    }

}
