package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.DialState
import com.sinasamaki.chroma.dial.IntervalOrientation
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.components.ExplodedView
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.PI

/**
 * The [TimelyDial]'s face taken apart into its separate drawing layers — the tick track, the
 * progress rings, the minute numbers and the time readout — and fanned out through [ExplodedView].
 *
 * A real [Dial] can't be exploded into planes like this, so instead an invisible [Dial] rides on
 * its own top-most layer: its track and thumb draw nothing, but it's a controlled dial driven by a
 * plain `degree` mutable state, which every other layer reads to stay in sync — so the whole stack
 * remains draggable. The rings layer paints a fake thumb, styled after [TimelyDial]'s real one, at
 * the invisible dial's live position (captured from its thumb slot) so the interaction reads
 * correctly even though the real thumb is invisible.
 *
 * With zero rotation and no [spacing] the stack collapses back into something that reads like the
 * ordinary dial; tilt and space it out to reveal how the face is composited.
 */
@Composable
fun ExplodedTimelyDial(
    swatch: Swatch,
    rotationX: Float,
    rotationY: Float,
    rotationZ: Float,
    spacing: Dp,
    modifier: Modifier = Modifier,
    initialDegree: Float = SNAPSHOT_DEGREE,
) {
    val measurer = rememberTextMeasurer()
    val degreeState = remember { mutableStateOf(initialDegree) }
    val liveDialState = remember { mutableStateOf<DialState?>(null) }

    LaunchedEffect(Unit) {
        var lastFrame = 0L
        while (true) {
            withInfiniteAnimationFrameNanos { frame ->
                if (lastFrame != 0L) {
                    val elapsedSeconds = (frame - lastFrame) / 1_000_000_000f
                    degreeState.value += elapsedSeconds * DEGREES_PER_SECOND
                }
                lastFrame = frame
            }
        }
    }

    val explode = (spacing / DECORATION_FULL_AT).coerceIn(0f, 1f)

    ExplodedView(
        rotationX = rotationX,
        rotationY = rotationY,
        rotationZ = rotationZ,
        spacing = spacing,
        modifier = modifier,
        decorator = { layer ->
            Box(
                Modifier
                    .border(
                        width = 1.dp,
                        color = swatch.v100.copy(alpha = .6f * explode),
                        shape = DecorationShape,
                    )
                    .innerShadow(shape = DecorationShape) {
                        color = White
                        radius = 60f
                        alpha = .2f * explode
                    }
                    .innerShadow(shape = DecorationShape) {
                        color = swatch.v200
                        radius = 24f
                        alpha = .4f * explode
                    }
                    .padding(16.dp)
            ) {
                layer()
            }
        },
    ) {
        layer { TrackTicksLayer(swatch, degreeState) }
        layer { RingsLayer(swatch, degreeState, liveDialState) }
        layer { NumbersLayer(measurer, degreeState) }
        layer {
            ReadoutLayer(swatch, degreeState.value)
            PlaybackButtonLayer(swatch)
            Dial(
                degree = degreeState.value,
                onDegreeChange = {},
                enabled = false,
                sweepDegrees = 99 * 360f,
                modifier = Modifier.fillMaxSize(),
                thumb = { state ->
                    liveDialState.value = state
                    Box(Modifier.size(60.dp))
                },
                track = {},
            )

        }
    }
}

/** The ring of second-ticks, each stretching outward as it nears the current position. */
@Composable
private fun TrackTicksLayer(swatch: Swatch, degreeState: State<Float>) {
    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                val reference = degreeState.value % 360f
                drawEveryInterval(
                    startDegrees = 0f,
                    sweepDegrees = 360f,
                    interval = 6f,
                    radius = size.width / 2f,
                    currentDegree = reference,
                    orientation = IntervalOrientation.PositionAndRotate,
                ) { data ->
                    val delta = (data.intervalDegree - reference + 180f).mod(360f) - 180f
                    val x = 1f - (delta.absoluteValue / 18f).coerceIn(0f, 1f)
                    val height = lerp(10.dp.toPx(), 40.dp.toPx(), x)
                    drawLine(
                        color = if (data.inActiveRange) swatch.v50
                        else swatch.v100.copy(alpha = .6f),
                        start = Offset(0f, 12.dp.toPx() - lerp(0f, 8.dp.toPx(), x)),
                        end = Offset(0f, 12.dp.toPx() - height),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
    )
}

/** The nested progress arcs — one per completed lap plus the partial current one. */
@Composable
private fun RingsLayer(swatch: Swatch, degreeState: State<Float>, dialState: State<DialState?>) {
    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                val degree = degreeState.value
                val rings = (degree / 360f).toInt()
                val range = 15
                val y = FastOutSlowInEasing.transform(
                    ((degree % 360f).coerceAtLeast(360f - range) - 360f).absoluteValue / range
                )
                for (i in 0..rings) {
                    val z = (rings - i + 1) - y
                    val sweep = (degree - (i * 360f)).coerceAtMost(360f)
                    val x = when {
                        sweep >= 360f -> 0f
                        else -> ((360f - sweep) / 30f).coerceIn(0f, 1f)
                    }
                    val padding = lerpStep(0.dp.toPx(), 12.dp.toPx(), z)
                    val stroke = androidx.compose.ui.unit.lerp(1.dp, 3.dp, x)
                    drawArc(
                        color = swatch.v100.copy(alpha = lerpStep(1f, -.15f, z)),
                        startAngle = 0f,
                        sweepAngle = sweep,
                        radius = center.x - 30.dp.toPx() - padding,
                        strokeWidth = stroke,
                    )
                }
            }
    ) {
        FakeThumb(swatch, dialState)
    }
}

/** A stand-in for [TimelyDial]'s real thumb, positioned from the invisible dial's [dialState]. */
@Composable
private fun BoxScope.FakeThumb(swatch: Swatch, dialState: State<DialState?>) {
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .graphicsLayer {
                val state = dialState.value
                val angle = if (state != null) state.absoluteDegree + state.overshootDegrees else 0f
                val angleInRadians = (angle - 90f) * PI.toFloat() / 180f
                val radius = state?.radius ?: 0f
                val thumbSize = state?.thumbSize ?: 0f
                val thumbRadius = radius - (thumbSize/2) - 0.dp.toPx()
                val centerOffset = state?.center ?: Offset.Zero
                val targetX = centerOffset.x + thumbRadius * cos(angleInRadians)
                val targetY = centerOffset.y + thumbRadius * sin(angleInRadians)
                translationX = targetX - thumbSize / 2f
                translationY = targetY - thumbSize / 2f
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 0.5f)
                alpha = if (thumbSize > 0f) 1f else 0f
            }
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
}

/** The 15/30/45/60 minute labels around the face, ducking inward as the dial sweeps over each. */
@Composable
private fun NumbersLayer(measurer: TextMeasurer, degreeState: State<Float>) {
    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                repeat(4) {
                    drawMinute(it, measurer, degreeState.value)
                }
            }
    )
}

private fun DrawScope.drawMinute(
    index: Int,
    measurer: TextMeasurer,
    absoluteDegree: Float,
) {
    val targetDegree = (index + 1) * 90f

    val delta = min(
        ((absoluteDegree % 360f) - targetDegree).absoluteValue,
        ((absoluteDegree % 360f) - (targetDegree % 360f)).absoluteValue,
    )

    val push = when {
        delta < 15f -> 1f - (delta / 15f)
        else -> 0f
    }
    val radiusMult = lerp(.385f, .345f, push)

    drawEveryInterval(
        startDegrees = targetDegree,
        sweepDegrees = 1f,
        interval = 1f,
        radius = size.width * radiusMult,
        orientation = IntervalOrientation.PositionOnly,
    ) { data ->
        if (data.index == 0) return@drawEveryInterval
        val result = measurer.measure(
            text = "${(index + 1) * 15}",
            style = TextStyle(
                color = Zinc50,
                fontSize = 18.sp,
                shadow = Shadow(
                    color = Black.copy(alpha = .4f),
                    blurRadius = 10f,
                ),
            ),
        )
        drawText(
            textLayoutResult = result,
            topLeft = Offset(
                -result.size.width / 2f,
                -result.size.height / 2f,
            ),
        )
    }
}

/** The minutes:seconds readout that floats over the centre of the dial. */
@Composable
private fun ReadoutLayer(swatch: Swatch, degree: Float) {
    val totalSeconds = (degree / 6f).roundToInt()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TimelyTime(
            left = totalSeconds / 60,
            right = totalSeconds % 60,
            color = swatch.v50,
            fontSize = 56.sp,
            strokeWidth = 3.dp,
        )
    }
}

/**
 * The same button [TimelyPlaybackControls] draws — shadowed circle with the pause glyph cut out of
 * it — parked above the readout on the same layer. Static rather than clickable: this dial climbs
 * on its own forever, so there's no play state to toggle back to, just the "currently running" look.
 */
@Composable
private fun PlaybackButtonLayer(swatch: Swatch) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize(.35f)
                .drawWithContent {
                    layer(size.toRect().inflate(size.width)) {
                        this@drawWithContent.drawContent()
                        val density = Density(density)
                        drawPath(
                            path = Path().apply {
                                addOutline(
                                    PlayTriangle.createOutline(size, layoutDirection, density)
                                )
                            },
                            color = Black,
                            blendMode = BlendMode.DstOut,
                        )
                    }
                }
                .dropShadow(shape = PlayTriangle) {
                    radius = 40f
                    spread = 15f
                    color = swatch.v200.copy(alpha = .9f)
                }
        )
    }
}

private fun lerpStep(start: Float, interval: Float, fraction: Float) = start + (interval * fraction)

private val DECORATION_FULL_AT = 48.dp
private val DecorationShape = RoundedCornerShape(0.dp)

private const val SNAPSHOT_DEGREE = 0f

private const val DEGREES_PER_SECOND = 120f
