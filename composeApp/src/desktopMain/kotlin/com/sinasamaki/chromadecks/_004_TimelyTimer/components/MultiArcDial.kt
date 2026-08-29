package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.IntervalOrientation
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlin.math.absoluteValue

/**
 * The dial as it stands after the multi-arc slides: same thumb, ring and ducking numbers as
 * [RingLinesDial], but the track can now stack one arc per completed hour.
 *
 * @param multiRing False draws a single naive arc that ignores extra turns (the "before" state at
 *   the top of [MultiArcSlide]); true draws one arc per completed hour, stacked and receding.
 * @param lerpTransition Whether the ring-boundary hand-off ([MultiArcTransitionSlide]'s subject)
 *   blends linearly across the last few degrees (smooth) or is left as a hard cut (visibly pops at
 *   360°).
 */
@Composable
fun MultiArcDial(
    swatch: Swatch,
    degree: Float,
    onDegreeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onDegreeChangeFinished: () -> Unit = {},
    sweepDegrees: Float = 99 * 360f,
    multiRing: Boolean = true,
    lerpTransition: Boolean = true,
    timer: Timer? = null,
) {
    val measurer = rememberTextMeasurer()

    Dial(
        degree = degree,
        onDegreeChange = onDegreeChange,
        onDegreeChangeFinished = onDegreeChangeFinished,
        sweepDegrees = sweepDegrees,
        enabled = timer?.isRunning != true,
        modifier = modifier.fillMaxHeight().aspectRatio(1f),
        thumb = {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.5f.dp)
                    .border(width = 2.5f.dp, color = swatch.v100, shape = CircleShape)
                    .padding(3.75f.dp)
                    .background(color = swatch.v100, shape = CircleShape)
                    .drawBehind {
                        for (i in 0..1) {
                            for (j in 0..3) {
                                drawCircle(
                                    color = swatch.v500,
                                    radius = 1.875f.dp.toPx(),
                                    center = (center - Offset(x = 7.5f.dp.toPx(), y = 2.5f.dp.toPx())) +
                                            Offset(x = j * 5.dp.toPx(), y = i * 5.dp.toPx()),
                                )
                            }
                        }
                    }
            )
        },
        track = { dialState ->
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawTickRing(dialState.degree, swatch)
                        repeat(4) { index -> drawMultiArcNumber(index, measurer, dialState.degree) }
                        if (multiRing) {
                            drawStackedArcs(dialState.degree, lerpTransition, swatch)
                        } else {
                            drawArc(
                                color = swatch.v50,
                                startAngle = 0f,
                                sweepAngle = dialState.degree.coerceIn(0f, 360f),
                                radius = center.x - 30.dp.toPx(),
                                strokeWidth = 3.dp,
                            )
                        }
                    }
            ) {
                if (timer != null) {
                    TimelyPlaybackControls(timer = timer, swatch = swatch)
                }
            }
        },
    )
}

private fun DrawScope.drawTickRing(currentDegree: Float, swatch: Swatch) {
    drawEveryInterval(
        startDegrees = 0f,
        sweepDegrees = 360f,
        interval = 6f,
        radius = size.width / 2f,
        currentDegree = currentDegree % 360f,
        orientation = IntervalOrientation.PositionAndRotate,
    ) { data ->
        val delta = ((data.intervalDegree - currentDegree) + 180f).mod(360f) - 180f
        val x = 1f - (delta.absoluteValue / 18f).coerceIn(0f, 1f)
        val height = lerp(10.dp.toPx(), 34.dp.toPx(), x)
        drawLine(
            color = if (data.inActiveRange) swatch.v50 else swatch.v100.copy(alpha = .6f),
            start = Offset(0f, 12.dp.toPx() - lerp(0f, 8.dp.toPx(), x)),
            end = Offset(0f, 12.dp.toPx() - height),
            strokeWidth = 2.dp.toPx(),
        )
    }
}

private fun DrawScope.drawMultiArcNumber(index: Int, measurer: TextMeasurer, currentDegree: Float) {
    val targetDegree = (index + 1) * 90f
    val distance = (((currentDegree - targetDegree) + 180f).mod(360f) - 180f).absoluteValue
    val push = (1f - distance / 15f).coerceIn(0f, 1f)
    val radiusFraction = lerp(.385f, .345f, push)

    drawEveryInterval(
        startDegrees = targetDegree,
        sweepDegrees = 1f,
        interval = 1f,
        radius = size.width * radiusFraction,
        orientation = IntervalOrientation.PositionOnly,
    ) { data ->
        if (data.index == 0) return@drawEveryInterval
        val result = measurer.measure(
            text = "${(index + 1) * 15}",
            style = TextStyle(
                color = Zinc50,
                fontSize = 18.sp,
                shadow = Shadow(color = Black.copy(alpha = .4f), blurRadius = 10f),
            ),
        )
        drawText(
            textLayoutResult = result,
            topLeft = Offset(-result.size.width / 2f, -result.size.height / 2f),
        )
    }
}

private fun lerpStep(start: Float, interval: Float, fraction: Float) = start + (interval * fraction)

/** One arc per completed hour, receding and fading with each ring further from the current one. */
private fun DrawScope.drawStackedArcs(
    absoluteDegree: Float,
    lerpTransition: Boolean,
    swatch: Swatch,
) {
    val rings = (absoluteDegree / 360f).toInt()

    for (i in 0..rings) {
        val range = 15
        val boundaryProgress = ((absoluteDegree % 360f).coerceAtLeast(360f - range) - 360f).absoluteValue / range
        val y = if (lerpTransition) boundaryProgress else boundaryProgress.roundToStep()
        val z = (rings - i + 1) - y

        val degree = (absoluteDegree - (i * 360f)).coerceAtMost(360f)
        val x = when {
            degree >= 360f -> 0f
            else -> ((360f - degree) / 30f).coerceIn(0f, 1f)
        }

        val padding = lerpStep(0.dp.toPx(), 12.dp.toPx(), z)
        val stroke = lerp(1.dp, 3.dp, x)

        drawArc(
            color = swatch.v100.copy(alpha = lerpStep(1f, -.15f, z)),
            startAngle = 0f,
            sweepAngle = degree,
            radius = center.x - 30.dp.toPx() - padding,
            strokeWidth = stroke,
        )
    }
}

private fun Float.roundToStep() = if (this >= .5f) 1f else 0f
