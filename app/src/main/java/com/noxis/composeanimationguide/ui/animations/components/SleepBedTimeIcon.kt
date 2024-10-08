package com.noxis.composeanimationguide.ui.animations.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.noxis.composeanimationguide.R


@Composable
internal fun SleepBedTimeIcon(isStart: Boolean, modifier: Modifier = Modifier) {
    val icon =
        if (isStart) painterResource(id = R.drawable.ic_bed) else painterResource(id = R.drawable.ic_alarm)

    Icon(
        painter = icon,
        contentDescription = null,
        tint = textSecondary,
        modifier = modifier
    )
}
