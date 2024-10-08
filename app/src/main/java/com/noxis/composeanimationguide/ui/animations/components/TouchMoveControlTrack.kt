package com.noxis.composeanimationguide.ui.animations.components

import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noxis.composeanimationguide.ui.animations.IconOffset
import com.noxis.composeanimationguide.ui.animations.adjustWhenLessThanZero
import com.noxis.composeanimationguide.ui.animations.adjustWithin360
import com.noxis.composeanimationguide.ui.animations.asCanvasArcStartAngle
import com.noxis.composeanimationguide.ui.animations.boldTextPaint
import com.noxis.composeanimationguide.ui.animations.clockLabels
import com.noxis.composeanimationguide.ui.animations.convertAngleToLocalDateTime
import com.noxis.composeanimationguide.ui.animations.convertHourToAngle
import com.noxis.composeanimationguide.ui.animations.convertTimeToAngle
import com.noxis.composeanimationguide.ui.animations.correctEndTimeNotConcerningDay
import com.noxis.composeanimationguide.ui.animations.getRotationAngleWithfixArcThreeOClock
import com.noxis.composeanimationguide.ui.animations.getSweepAngle
import com.noxis.composeanimationguide.ui.animations.isClockBoldNeeded
import com.noxis.composeanimationguide.ui.animations.normalTextPaint
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun TouchMoveControlTrack(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    startTimeUpdateInvoker: (LocalDateTime) -> Unit,
    endTimeUpdateInvoker: (LocalDateTime) -> Unit,
) {

    var updateStartTime by remember {
        mutableStateOf(startTime)
    }

    var updateEndTime by remember {
        mutableStateOf(endTime)
    }
    val sweepAngleForKnob = remember {
        mutableFloatStateOf(convertHourToAngle(updateStartTime, updateEndTime))
    }
    var startIconAngle by remember {
        mutableFloatStateOf(convertTimeToAngle(updateStartTime))
    }
    var endIconAngle by remember {
        mutableFloatStateOf(convertTimeToAngle(updateEndTime))
    }

    val scope = rememberCoroutineScope()

    val clockRadius = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.div(3.5).dp.toPx()
    }

    val clockRadiusDp = LocalConfiguration.current.screenWidthDp.div(3.5).dp

    val knobTrackStrokeWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.div(6).dp.toPx()
    }

    val knobStrokeWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.div(8).dp.toPx()
    }

    var shapeCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var radius by remember {
        mutableFloatStateOf(0f)
    }

    val reduceOffsetIcon = with(LocalDensity.current) {
        24.dp.toPx()
    }

    val haptic = LocalHapticFeedback.current

    Box {
        Canvas(modifier = Modifier.size(300.dp), onDraw = {
            drawKnobBackground(knobTrackStrokeWidth)
            drawClockCircle(clockRadius, shapeCenter)
        })

        DrawTicks(clockRadiusDp)

        Canvas(modifier = Modifier
            .size(300.dp)
            .pointerInput(Unit) {
                var timeAtTouchScroll: LocalDateTime? = null
                var angleOnTouchWhenStarted: Float? = null
                var isStartWithStartIcon: Boolean? = null
                var isStartWithEndIcon: Boolean? = null
                scope.launch {
                    detectDragGestures(
                        onDragEnd = { timeAtTouchScroll = null },
                        onDragStart = { offset ->
                            angleOnTouchWhenStarted =
                                getRotationAngleWithfixArcThreeOClock(offset, shapeCenter)
                            timeAtTouchScroll =
                                convertAngleToLocalDateTime(angleOnTouchWhenStarted!!)
                            isStartWithStartIcon =
                                Duration
                                    .between(timeAtTouchScroll!!, updateStartTime)
                                    .toMinutes() in -30..30
                            isStartWithEndIcon =
                                Duration
                                    .between(
                                        timeAtTouchScroll!!,
                                        correctEndTimeNotConcerningDay(
                                            timeAtTouchScroll,
                                            updateEndTime
                                        )
                                    )
                                    .toMinutes() in -30..30
                        },
                        onDrag = { change, _ ->
                            var startAngleKnob = getRotationAngleWithfixArcThreeOClock(
                                change.position,
                                shapeCenter
                            ).adjustWithin360()

                            val endingAngle: Float

                            when {
                                isStartWithStartIcon == true -> {
                                    // user touched the start icon.
                                    Log.e("touch start", "user touched the start icon.")
                                    endingAngle = convertTimeToAngle(updateEndTime)
                                }

                                isStartWithEndIcon == true -> {
                                    Log.e("touch end", "user touched the end icon.")
                                    endingAngle = startAngleKnob
                                    startAngleKnob = convertTimeToAngle(updateStartTime)

                                }

                                else -> {
                                    val angleTouchStarted = convertTimeToAngle(timeAtTouchScroll!!)
                                    val angleSweepedAfterMove =
                                        startAngleKnob
                                            .minus(angleTouchStarted)
                                            .times(0.5f)
                                    Log.e("angleSweepedAfterMove", "$angleSweepedAfterMove")
                                    // TODO fix this check so we can calculate the correct startAngle for knob
                                    startAngleKnob =
                                        (convertTimeToAngle(updateStartTime) + angleSweepedAfterMove)
                                    endingAngle = startAngleKnob
                                        .plus(
                                            convertHourToAngle(
                                                updateStartTime,
                                                updateEndTime
                                            )
                                        )
                                        .adjustWhenLessThanZero()
                                    Log.e("touch between", "user touched somewhere in between")
                                }
                            }

                            val startTimeCalc = convertAngleToLocalDateTime(startAngleKnob)
                            var endTimeCalc = convertAngleToLocalDateTime(endingAngle)
                            updateStartTime = startTimeCalc

                            val amPmEndTime = endTimeCalc.format(DateTimeFormatter.ofPattern("a"))
                            val amPmStartTime =
                                startTimeCalc.format(DateTimeFormatter.ofPattern("a"))

                            if (amPmEndTime.equals("am", ignoreCase = true)) {
                                if (!amPmStartTime.equals("am", ignoreCase = true)) {
                                    endTimeCalc = endTimeCalc.plusDays(1)
                                }
                            }
                            updateEndTime = endTimeCalc

                            startTimeUpdateInvoker.invoke(updateStartTime)
                            endTimeUpdateInvoker.invoke(updateEndTime)

                            Log.e("on Move", "$startTimeCalc $endTimeCalc")

                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                            change.consumeAllChanges()
                            change.consume()

                        }
                    )
                }

            }, onDraw = {
            shapeCenter = center

            radius = size.minDimension / 2

            startIconAngle = convertTimeToAngle(startTime)
            endIconAngle = convertTimeToAngle(endTime)

            val sweepAngle =
                getSweepAngle(updateStartTime, updateEndTime, endIconAngle, startIconAngle)

            drawRotatingKnob(
                startIconAngle,
                knobStrokeWidth,
                sweepAngle
            )
        })

        val sweepAngle = getSweepAngle(updateStartTime, updateEndTime, endIconAngle, startIconAngle)

        DrawHandleLinesOnTheKnob(
            clockRadiusDp,
            knobTrackStrokeWidth,
            sweepAngle,
            startIconAngle
        )

        Box(
            Modifier
                .size(300.dp)
        ) {
            KnobIcon(
                reduceOffsetIcon,
                shapeCenter,
                true,
                startIconAngle,
                radius,

                )
            KnobIcon(
                reduceOffsetIcon,
                shapeCenter,
                false,
                endIconAngle,
                radius,

                )
        }

    }

}

@Composable
fun KnobIcon(
    reduceOffsetIcon: Float,
    shapeCenter: Offset,
    isStart: Boolean,
    angleKnob: Float,
    radius: Float
) {
    val angleKnob =
        angleKnob.minus(90f)// .minus(90f),fix startAngle - Starting angle in degrees. 0 represents 3 o'clock
    // start icon offset
    val iconX = (shapeCenter.x + cos(Math.toRadians(angleKnob.toDouble())) * radius).toFloat()
    val iconY = (shapeCenter.y + sin(Math.toRadians(angleKnob.toDouble())) * radius).toFloat()
    val iconOffset = Offset(iconX, iconY)

    SleepBedTimeIcon(isStart,
        Modifier.offset {
            IconOffset(iconOffset, reduceOffsetIcon)
        })
}


@Composable
private fun BoxScope.DrawHandleLinesOnTheKnob(
    clockRadiusDp: Dp,
    knobTrackStrokeWidth: Float,
    sweepAngle: Float,
    startIconAngle: Float
) {
    val handleLinesCount = sweepAngle.div(3).toInt()
    Box(
        modifier = Modifier
            .rotate(startIconAngle)
            .align(Alignment.Center)
            .size(clockRadiusDp.plus(knobTrackStrokeWidth.div(2).dp))
    ) {
        repeat(handleLinesCount) {
            if (it > 3 && it < handleLinesCount.minus(3)) {
                Handle(it, sweepAngle, handleLinesCount)
            }
        }
    }
}

@Composable
fun BoxScope.Handle(handle: Int, sweepAngle: Float, handleLinesCount: Int) {
    val angle = sweepAngle / handleLinesCount * handle

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .width(3.dp)
            .height(14.dp)
            .rotate(angle)
            .offset(0.dp, (-150).dp)
            .background(Color(1, 0, 0).copy(alpha = 0.6f))
    )
}

private fun DrawScope.drawRotatingKnob(
    startAngle: Float,
    knobStrokeWidth: Float,
    sweepAngleForKnob: Float
) {

    drawArc(
        color = Color(45, 44, 46),
        startAngle = startAngle.asCanvasArcStartAngle(),
        sweepAngle = sweepAngleForKnob,
        false,
        style = Stroke(width = knobStrokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

@Composable
private fun BoxScope.DrawTicks(clockRadius: Dp) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(clockRadius)
    ) {
        repeat(120) {
            Tick(it)
        }
    }
}

@Composable
private fun BoxScope.Tick(tick: Int) {
    val angle = 360f / 120f * tick
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .width(if (tick % 5 == 0) 1.5.dp else 1.dp)
            .height(if (tick % 5 == 0) 6.dp else 2.dp)
            .rotate(angle)
            .offset(0.dp, (-110).dp)
            .background(textSecondary.copy(alpha = 0.5f)),
    )
}


private fun DrawScope.drawClockCircle(clockRadius: Float, shapeCenter: Offset) {
    drawCircle(color = Color(45, 44, 46), radius = clockRadius)
    drawClockNumerals(shapeCenter, clockRadius)
}

private fun DrawScope.drawClockNumerals(shapeCenter: Offset, clockRadius: Float) {
    val labels = clockLabels()
    labels.forEachIndexed { index, it ->
        val paint = normalTextPaint()
        val boldPaint = boldTextPaint()
        val rect = Rect()
        paint.getTextBounds(it, 0, it.length, rect)
        val angle = index * Math.PI * 2 / 24 - (Math.PI / 2)
        val x = (shapeCenter.x + cos(angle) * clockRadius.times(0.75f) - rect.width() / 2).toFloat()
        val y =
            (shapeCenter.y + sin(angle) * clockRadius.times(0.78f) + rect.height() / 2).toFloat()
        if (isClockBoldNeeded(it) || it.toInt() % 2 == 0) {
            drawContext.canvas.nativeCanvas.drawText(
                it,
                x,
                y, if (isClockBoldNeeded(it)) boldPaint else paint
            )
        }
    }
}

private fun DrawScope.drawKnobBackground(knobTrackStrokeWidth: Float) {
    drawArc(
        color = Color(1, 0, 0),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(width = knobTrackStrokeWidth)
    )
}
