package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.IntervalOrientation
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlin.math.absoluteValue

/**
 * How the demonstration numbers react as the thumb sweeps over their angle.
 *
 * This is the whole point of the slide: each mode is a progressively richer answer to "when should
 * a number duck inward?", building up to what [TimelyDial] actually ships.
 */
enum class NumberDuckMode {
    /** No ducking at all — numbers sit at rest regardless of the thumb. */
    NONE,

    /** Binary: ducks only on the single integer degree it sits on ([degree.toInt] == target). */
    POP_EXACT,

    /** Binary, but over a window of degrees either side of the target. */
    POP_RANGE,

    /** Continuous: eases inward as the thumb nears the target and back out as it leaves. */
    SMOOTH,
}

private const val NUMBER_INTERVAL = 90f
private const val NUMBER_COUNT = 4

private const val NUMBER_RADIUS_FRACTION = .385f
private const val DUCK_DISTANCE_FRACTION = .04f

/**
 * A demonstration twin of [TimelyDial], deliberately kept separate so the real dial stays clean.
 * It mirrors the real dial's look — full set of minute numbers, the dotted thumb, and the
 * snap-on-release glide — but routes every number's inward duck through [duckMode] so the
 * presentation can dismantle the behaviour one rule at a time.
 */
@Composable
fun DuckingNumberDial(
    swatch: Swatch,
    duckMode: NumberDuckMode,
    modifier: Modifier = Modifier,
    showTicks: Boolean = true,
    numbersVisible: Boolean = true,
) {
    val scope = rememberCoroutineScope()
    val timer = remember { Timer(scope).apply { remainingSeconds = 720f } }
    val measurer = rememberTextMeasurer()

    LaunchedEffect(timer.isRunning) {
        if (timer.isRunning) timer.runCountdown()
    }

    val appear = List(NUMBER_COUNT) { index ->
        val appearState by animateFloatAsState(
            targetValue = if (numbersVisible) 1f else 0f,
            animationSpec = tween(durationMillis = 350, delayMillis = index * 90),
            label = "numberAppear$index",
        )
        appearState
    }

    val window = 15f

    Dial(
        degree = timer.degrees,
        onDegreeChange = timer::onDegreeChange,
        onDegreeChangeFinished = timer::onDegreeChangeFinished,
        sweepDegrees = 360f,
        enabled = !timer.isRunning,
        modifier = modifier.size(400.dp),
        thumb = {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.5f.dp)
                    .border(
                        width = 2.5f.dp,
                        color = swatch.v100,
                        shape = CircleShape,
                    )
                    .padding(3.75f.dp)
                    .background(
                        color = swatch.v100,
                        shape = CircleShape,
                    )
                    .drawBehind {
                        for (i in 0..1) {
                            for (j in 0..3) {
                                drawCircle(
                                    color = swatch.v500,
                                    radius = (1.875f).dp.toPx(),
                                    center = (center - Offset(
                                        x = (7.5f).dp.toPx(),
                                        y = 2.5f.dp.toPx(),
                                    )) + Offset(
                                        x = j * 5.dp.toPx(),
                                        y = i * 5.dp.toPx(),
                                    )
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
                        if (showTicks) {
                            drawTrackArc(dialState.degree, swatch)
                        }
                        drawDuckingNumbers(
                            currentDegree = dialState.degree,
                            duckMode = duckMode,
                            window = window,
                            measurer = measurer,
                            appear = appear,
                        )
                    }
            ) {
                TimelyPlaybackControls(timer = timer, swatch = swatch)
            }
        },
    )
}

/** 0f (at rest, further out) → 1f (fully ducked in), per the selected [mode]. */
private fun duckPush(
    mode: NumberDuckMode,
    currentDegree: Float,
    targetDegree: Float,
    window: Float,
): Float = when (mode) {
    NumberDuckMode.NONE -> 0f

    NumberDuckMode.POP_EXACT ->
        if (currentDegree.toInt() == targetDegree.toInt()) 1f else 0f

    NumberDuckMode.POP_RANGE ->
        if (currentDegree.toInt() in (targetDegree - window).toInt()..(targetDegree + window).toInt()) 1f else 0f

    NumberDuckMode.SMOOTH -> {
        val distance = (currentDegree - targetDegree).absoluteValue
        (1f - distance / window).coerceIn(0f, 1f)
    }
}

/**
 * Places all four minute labels in one pass: a single [drawEveryInterval] stepping every 90°, so
 * every number is accounted for by its index rather than by a hand-rolled list of targets. Index 0
 * is 12 o'clock — the same point index 4 (360°) lands on — so it's skipped and the labels run
 * 15 / 30 / 45 / 60.
 *
 * Each label slides inward by its own [duckPush], applied as a translation toward the dial's
 * centre. [appear] fades a label in in place — no scale, no translate.
 */
private fun DrawScope.drawDuckingNumbers(
    currentDegree: Float,
    duckMode: NumberDuckMode,
    window: Float,
    measurer: TextMeasurer,
    appear: List<Float>,
) {
    val radius = size.width * NUMBER_RADIUS_FRACTION
    val duckDistance = size.width * DUCK_DISTANCE_FRACTION

    drawEveryInterval(
        interval = NUMBER_INTERVAL,
        radius = radius,
        orientation = IntervalOrientation.PositionOnly,
    ) { data ->
        if (data.index == 0) return@drawEveryInterval

        val labelAlpha = appear[data.index - 1]
        if (labelAlpha <= 0f) return@drawEveryInterval

        val push = duckPush(duckMode, currentDegree, data.intervalDegree, window)
        val inward = (center - data.position) / radius

        val result = measurer.measure(
            text = "${data.index * 15}",
            style = TextStyle(
                color = Zinc50.copy(alpha = labelAlpha),
                fontSize = 18.sp,
                shadow = Shadow(
                    color = Black.copy(alpha = .4f * labelAlpha),
                    blurRadius = 10f,
                ),
            ),
        )
        drawText(
            textLayoutResult = result,
            topLeft = inward * (duckDistance * push) + Offset(
                -result.size.width / 2f,
                -result.size.height / 2f,
            ),
        )
    }
}

/** The plain arc from Arc.kt — the ring of ticks hasn't been introduced at this point yet. */
private fun DrawScope.drawTrackArc(
    currentDegree: Float,
    swatch: Swatch,
) {
    val radius = size.width / 2f - 30.dp.toPx()
    drawArc(
        color = swatch.v100.copy(alpha = .3f),
        startAngle = 0f,
        sweepAngle = 360f,
        radius = radius,
    )
    drawArc(
        color = swatch.v50,
        startAngle = 0f,
        sweepAngle = currentDegree,
        radius = radius,
    )
}
