package com.noxis.composeanimationguide.ui.animations

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.atan2

internal fun Float.adjustWithin360(): Float {
    // adjust the angle
    return if (this > 360) {
        this.minus(360)
    } else {
        this
    }
}

internal fun correctEndTimeNotConcerningDay(
    timeAtTouchScroll: LocalDateTime?,
    endTimeValue: LocalDateTime
) = if (timeAtTouchScroll!!.toLocalDate().isBefore(endTimeValue.toLocalDate())) {
    endTimeValue.minusDays(1)
} else {
    endTimeValue
}

internal fun getRotationAngleWithfixArcThreeOClock(offset: Offset, shapeCenter: Offset): Float {
    val theta = radians(offset, shapeCenter)

    var angle = Math.toDegrees(theta)

    if (angle < 0) {
        angle += 360.0
    }
    return angle.toFloat().fixArcThreeOClock()
}

private fun radians(
    currentPosition: Offset,
    center: Offset
): Double {
    val (dx, dy) = currentPosition - center
    return atan2(dy, dx).toDouble()
}

/**
 * .plus(90f),fix startAngle - Starting angle in degrees. 0 represents 3 o'clock
 */
private fun Float.fixArcThreeOClock(): Float = this.plus(90f)


internal fun isClockBoldNeeded(it: String): Boolean {
    return it.contains("AM", ignoreCase = true) || it.contains(
        "PM",
        ignoreCase = true
    )
}

internal fun boldTextPaint(): Paint {
    return Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 32f
        this.isFakeBoldText = true
    }
}

internal fun normalTextPaint(): Paint {
    return Paint().apply {
        color = android.graphics.Color.LTGRAY
        textSize = 32f
    }
}

internal fun clockLabels() = arrayOf(
    "12AM",
    "1",
    "2",
    "3",
    "4",
    "5",
    "6AM",
    "7",
    "8",
    "9",
    "10",
    "11",
    "12PM",
    "1",
    "2",
    "3",
    "4",
    "5",
    "6PM",
    "7",
    "8",
    "9",
    "10",
    "11"
)

internal fun convertAngleToLocalDateTime(startAngle: Float): LocalDateTime {
    var startAngle = startAngle
    while (startAngle > 360) {
        startAngle = startAngle.minus(360f)
    }
    var decimalValue = startAngle.div(
        DEGREE_IN_HOUR
    )
    if (decimalValue < 0) decimalValue += 12.0f
    val hours = decimalValue.toInt()
    val minutes = (decimalValue * MINUTES_IN_HOUR).toInt() % MINUTES_IN_HOUR
    return LocalDateTime.of(LocalDate.now(), LocalTime.of(hours, minutes))
}

internal fun convertTimeToAngle(startTime: LocalDateTime): Float {
    return startTime.hour.times(DEGREE_IN_HOUR) + startTime.minute.times(DEGREE_IN_HOUR / MINUTES_IN_HOUR)
}

internal fun convertHourToAngle(startTime: LocalDateTime, endTime: LocalDateTime): Float {
    val angle =
        startTime.hour.times(DEGREE_IN_HOUR) + startTime.minute.times(DEGREE_IN_HOUR / MINUTES_IN_HOUR)
    val endAngle =
        endTime.hour.times(DEGREE_IN_HOUR) + endTime.minute.times(DEGREE_IN_HOUR / MINUTES_IN_HOUR)
    return endAngle.minus(angle)
}

internal fun Float.asCanvasArcStartAngle(): Float {
    return this.minus(90f)
}

internal fun Float.adjustWhenLessThanZero(): Float {
    return if (this < 0) {
        360.plus(this)
    } else {
        this
    }
}

internal fun getSweepAngle(
    startTimeValue: LocalDateTime,
    endTimeValue: LocalDateTime,
    endIconAngle: Float,
    startIconAngle: Float
) = if (startTimeValue.dayOfMonth < endTimeValue.dayOfMonth) {
    // adjust the sweepAngle
    endIconAngle.plus(360.minus(startIconAngle))
} else {
    endIconAngle.minus(startIconAngle)
}

internal fun IconOffset(
    startIconOffset: Offset,
    reduceOffsetIcon: Float
) = IntOffset(
    startIconOffset.x
        .toInt()
        .minus(reduceOffsetIcon / 2)
        .toInt(),
    startIconOffset.y
        .toInt()
        .minus(reduceOffsetIcon / 2)
        .toInt()
)


const val DEGREE_IN_HOUR = 15f
const val MINUTES_IN_HOUR = 60
