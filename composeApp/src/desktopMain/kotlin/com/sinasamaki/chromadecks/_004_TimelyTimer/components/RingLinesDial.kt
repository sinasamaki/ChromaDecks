package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.IntervalOrientation
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.kotlinconf.logo.strokeWidth
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

/**
 * A demonstration twin of [TimelyDial] for the ring-of-lines slide: the arc, ducking numbers and
 * thumb are already a given (matching the real dial's finished [NumberDuckMode.SMOOTH] behaviour),
 * and this composable adds the two capabilities that slide teaches — a tick ring revealed as a
 * radial wipe, then a thumb-anchored wedge (from the dial's true centre to the active range on the
 * ring) whose ticks stretch by proximity.
 *
 * @param ringVisible Sweeps the tick ring in, staggered around the circle.
 * @param rangeVisible Once true, the range indicator fades in and follows the thumb around the ring.
 * @param stretchVisible Once true, ticks inside that range lengthen by proximity to the thumb.
 */
@Composable
fun RingLinesDial(
    swatch: Swatch,
    ringVisible: Boolean,
    rangeVisible: Boolean,
    stretchVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val timer = remember { Timer(scope).apply { remainingSeconds = 420f } }
    val measurer = rememberTextMeasurer()

    LaunchedEffect(timer.isRunning) {
        if (timer.isRunning) timer.runCountdown()
    }

    val ringReveal by animateFloatAsState(
        targetValue = if (ringVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1400),
        label = "ringReveal",
    )
    val rangeReveal by animateFloatAsState(
        targetValue = if (rangeVisible) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "rangeReveal",
    )
    val stretchReveal by animateFloatAsState(
        targetValue = if (stretchVisible) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "stretchReveal",
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Dial(
            degree = timer.degrees,
            onDegreeChange = timer::onDegreeChange,
            onDegreeChangeFinished = timer::onDegreeChangeFinished,
            sweepDegrees = 360f,
            enabled = !timer.isRunning,
            modifier = Modifier.size(400.dp),
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
                                        center = (center - Offset(
                                            x = 7.5f.dp.toPx(),
                                            y = 2.5f.dp.toPx()
                                        )) +
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

                            if (rangeReveal > 0f) {
                                drawCenterWedge(dialState.degree, rangeReveal, swatch)
                            }

                            val radius = size.width / 2f - 30.dp.toPx()
                            drawArc(
                                color = swatch.v100.copy(alpha = .3f),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                radius = radius,
                                strokeWidth = 3.dp
                            )
                            drawArc(
                                color = swatch.v50,
                                startAngle = 0f,
                                sweepAngle = dialState.degree,
                                radius = radius,
                                strokeWidth = 3.dp
                            )

                            if (ringReveal > 0f) {
                                drawTickRing(dialState.degree, ringReveal, stretchReveal, swatch)
                            }

                            repeat(4) { index ->
                                drawRingLinesNumber(index, measurer, dialState.degree)
                            }
                        }
                ) {
                    TimelyPlaybackControls(timer = timer, swatch = swatch)
                }
            },
        )
    }
}

/**
 * A wedge from the Dial's true centre out to the two edges of the active range on the ring —
 * exact geometry (real centre, real radius, real degree), not a repositioned side-diagram, so it
 * always converges precisely at the centre no matter where the thumb sits.
 */
private fun DrawScope.drawCenterWedge(
    currentDegree: Float,
    reveal: Float,
    swatch: Swatch,
) {
    val halfWidth = lerp(0f, 18f, reveal)
    val ringRadius = size.width / 2f - 30.dp.toPx() //+ (1.5f/2).dp.toPx()

    fun pointAt(degree: Float): Offset {
        val rad = (degree - 90f) * (PI.toFloat() / 180f)
        return center + Offset(cos(rad) * ringRadius, sin(rad) * ringRadius)
    }

    val left = pointAt(currentDegree - halfWidth)
    val right = pointAt(currentDegree + halfWidth)

    val path = Path().apply {
        moveTo(center.x, center.y)
        arcTo(
            rect = Rect(center = center, radius = ringRadius),
            startAngleDegrees = currentDegree - halfWidth - 90f,
            sweepAngleDegrees = halfWidth * 2f,
            forceMoveTo = false,
        )
        close()
    }
    drawPath(
        path = path,
        brush = Brush.radialGradient(
            colors = listOf(
                Transparent,
                Transparent,
                swatch.v100.copy(alpha = .4f * reveal),
            ),
            center = center,
            radius = ringRadius,
        ),
    )
    drawPath(
        path = path,
        style = Stroke(
            width = 1f.dp.toPx()
        ),
        brush = Brush.radialGradient(
            colors = listOf(
                Transparent,
                swatch.v50.copy(alpha = .1f * reveal),
                swatch.v50.copy(alpha = 1f * reveal),
            ),
            center = center,
            radius = ringRadius,
        ),
    )
}

/** A ring of ticks swept in by [ringReveal]; once [stretchReveal] > 0 they stretch by proximity. */
private fun DrawScope.drawTickRing(
    currentDegree: Float,
    ringReveal: Float,
    stretchReveal: Float,
    swatch: Swatch,
) {
    val revealThreshold = ringReveal * 390f

    drawEveryInterval(
        startDegrees = 0f,
        sweepDegrees = 360f,
        interval = 6f,
        radius = size.width / 2f,
        currentDegree = currentDegree % 360f,
        orientation = IntervalOrientation.PositionAndRotate,
    ) { data ->
        val appear = ((revealThreshold - data.intervalDegree) / 30f).coerceIn(0f, 1f)
        if (appear <= 0f) return@drawEveryInterval

        val delta = ((data.intervalDegree - currentDegree) + 180f).mod(360f) - 180f
        val x = 1f - (delta.absoluteValue / 18f).coerceIn(0f, 1f)
        val proximityHeight = lerp(10.dp.toPx(), 40.dp.toPx(), x)
        val height = lerp(10.dp.toPx(), proximityHeight, stretchReveal)

        drawLine(
            color = if (data.inActiveRange) swatch.v50 else swatch.v100.copy(alpha = .6f * appear),
            start = Offset(0f, 12.dp.toPx() - lerp(0f, 8.dp.toPx(), x * stretchReveal)),
            end = Offset(0f, 12.dp.toPx() - height),
            strokeWidth = 2.dp.toPx(),
        )
    }
}

/** The four minute labels — always on, ducking with the finished [NumberDuckMode.SMOOTH] rule. */
private fun DrawScope.drawRingLinesNumber(
    index: Int,
    measurer: TextMeasurer,
    currentDegree: Float,
) {
    val targetDegree = (index + 1) * 90f
    val distance = (currentDegree - targetDegree).absoluteValue
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
