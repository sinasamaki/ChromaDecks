package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.IntervalOrientation
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

class Timer(private val scope: CoroutineScope) {

    private var settleJob: Job? = null

    var remainingSeconds by mutableStateOf(0f)

    var isRunning by mutableStateOf(false)
        private set

    var isDragging by mutableStateOf(false)
        private set

    val totalSeconds: Int
        get() = if (isDragging) (remainingSeconds / 60f).roundToInt() * 60
        else ceil(remainingSeconds).toInt()
    val hours: Int get() = totalSeconds / 3600
    val minutes: Int get() = (totalSeconds % 3600) / 60
    val seconds: Int get() = totalSeconds % 60
    val totalMinutes: Int get() = totalSeconds / 60

    fun start() {
        if (remainingSeconds > 0f) isRunning = true
    }

    fun pause() {
        isRunning = false
    }

    fun stop() {
        isRunning = false
        remainingSeconds = 0f
    }

    suspend fun runCountdown() {
        var lastFrame = 0L
        while (isRunning && remainingSeconds > 0f) {
            withFrameNanos { frame ->
                if (lastFrame != 0L) {
                    val elapsed = (frame - lastFrame) / 1_000_000_000f
                    remainingSeconds = (remainingSeconds - elapsed).coerceAtLeast(0f)
                }
                lastFrame = frame
            }
        }
        isRunning = false
    }

    fun onDegreeChange(degree: Float) {
        settleJob?.cancel()
        if (isRunning) pause()
        isDragging = true
        remainingSeconds = (degree * 10f).coerceAtLeast(0f)
    }

    fun onDegreeChangeFinished() {
        settleJob = scope.launch { settleToNearestMinute() }
    }

    private suspend fun settleToNearestMinute() {
        val start = remainingSeconds
        val target = (start / 60f).roundToInt().toFloat().times(60f).coerceAtLeast(0f)
        if (start == target) {
            isDragging = false
            return
        }
        Animatable(start).animateTo(
            targetValue = target,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        ) {
            remainingSeconds = value
        }
        isDragging = false
    }

    val degrees: Float
        get() = remainingSeconds * .1f
}

@Composable
fun TimelyDial(
    modifier: Modifier = Modifier,
    swatch: Swatch,
) {

    val scope = rememberCoroutineScope()
    val timer = remember { Timer(scope) }

    LaunchedEffect(timer.isRunning) {
        if (timer.isRunning) timer.runCountdown()
    }

    Dial(
        degree = timer.degrees,
        onDegreeChange = timer::onDegreeChange,
        onDegreeChangeFinished = timer::onDegreeChangeFinished,
        sweepDegrees = 99 * 360f,
        enabled = !timer.isRunning,
        modifier = modifier
            .size(400.dp),
        thumb = {
            val thumbScale by animateFloatAsState(
                targetValue = if (timer.isRunning) 0f else 1f,
                label = "thumbScale",
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(10.dp)
                    .scale(thumbScale)
                    .border(
                        width = 2.dp,
                        color = swatch.v100,
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .background(
                        color = swatch.v100,
                        shape = CircleShape,
                    )
                    .drawBehind {
                        for (i in 0..1) {
                            for (j in 0..3) {
                                drawCircle(
                                    color = swatch.v500,
                                    radius = (1.5f).dp.toPx(),
                                    center = (center - Offset(
                                        x = (6f).dp.toPx(),
                                        y = 2.dp.toPx(),
                                    )) + Offset(
                                        x = j * 4.dp.toPx(),
                                        y = i * 4.dp.toPx(),
                                    )
                                )
                            }
                        }
                    }
            )
        },
        track = { dialState ->
            val measurer = rememberTextMeasurer()
            val secInfluence by animateFloatAsState(
                targetValue = if (timer.isRunning) 1f else 0f,
                label = "secInfluence",
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val secDegree = (dialState.degree / 6f).mod(1f) * 360f
                        drawEveryInterval(
                            startDegrees = 0f,
                            sweepDegrees = 360f,
                            interval = 6f,
                            radius = size.width / 2f,
                            currentDegree = dialState.degree % 360f,
                            orientation = IntervalOrientation.PositionAndRotate,
                        ) { data ->
                            val delta =
                                ((data.intervalDegree - dialState.overshootDegrees) - dialState.absoluteDegree + 180f)
                                    .mod(360f) - 180f
                            val x =
                                1f - (delta.absoluteValue / 18f).coerceIn(0f..1f)
                            val height = lerp(10.dp.toPx(), 40.dp.toPx(), x)
                            drawLine(
                                color = if (data.inActiveRange) swatch.v50 else swatch.v100.copy(
                                    alpha = .6f
                                ),
                                start = Offset(0f, 12.dp.toPx() - lerp(0f, 8.dp.toPx(), x)),
                                end = Offset(0f, 12.dp.toPx() - height),
                                strokeWidth = 2.dp.toPx(),
                            )

                            val secDelta =
                                (data.intervalDegree - secDegree + 180f)
                                    .mod(360f) - 180f
                            val secX =
                                1f - (secDelta.absoluteValue / 18f).coerceIn(0f..1f)
                            val inwardHeight =
                                lerp(0f, 40.dp.toPx(), secX) * secInfluence
                            drawLine(
                                color = if (data.inActiveRange) swatch.v50 else swatch.v100.copy(
                                    alpha = .6f
                                ),
                                start = Offset(0f, 12.dp.toPx() - lerp(0f, 8.dp.toPx(), x)),
                                end = Offset(0f, 12.dp.toPx() + inwardHeight),
                                strokeWidth = 2.dp.toPx(),
                            )
                        }

                        repeat(4) {
                            drawMinute(it, measurer, dialState.degree, secInfluence, secDegree)
                        }

                        val rings = (dialState.degree / 360f).toInt()

                        for (i in 0..rings) {

                            val range = 15
                            val y = FastOutSlowInEasing.transform(
                                ((dialState.degree % 360f).coerceAtLeast(360f - range) - 360f).absoluteValue / range
                            )
                            val z = (rings - i + 1) - y

                            val degree = (dialState.degree - (i * 360f)).coerceAtMost(360f)
                            val x = when {
                                degree >= 360f -> 0f
                                else -> ((360f - degree) / 30f).coerceIn(0f..1f)
                            }

                            val padding = lerpStep(
                                0.dp.toPx(),
                                12.dp.toPx(),
                                z
                            )

                            val stroke = androidx.compose.ui.unit.lerp(
                                1.dp,
                                3.dp,
                                x
                            )

                            drawArc(
                                color = swatch.v100.copy(
                                    alpha = lerpStep(1f, -.15f, z)
                                ),
                                startAngle = 0f,
                                sweepAngle = degree,
                                radius = center.x - 24.dp.toPx() - padding,
                                strokeWidth = stroke
                            )
                        }
                    }
            ) {
                TimelyPlaybackControls(timer = timer, swatch = swatch)
            }
        },
    )
}

/**
 * The play/pause button and hours:minutes:seconds readout centred over any dial driven by
 * [timer]. Shared by [TimelyDial] and the demo dials in the later slides so they all end up
 * with the same functional playback controls once the timer is introduced.
 */
@Composable
fun BoxScope.TimelyPlaybackControls(
    timer: Timer,
    swatch: Swatch,
    showButton: Boolean = timer.totalSeconds > 0,
    showTime: Boolean = true,
) {
    if (showButton) {
        val centerShape by remember {
            derivedStateOf {
                if (timer.isRunning) PauseShape else PlayTriangle
            }
        }
        val interaction = remember { MutableInteractionSource() }
        val isHovered by interaction.collectIsHoveredAsState()
        val isPressed by interaction.collectIsPressedAsState()
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(.35f)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                ) {
                    if (timer.isRunning) timer.pause() else timer.start()
                }
                .drawWithContent {
                    layer(
                        size.toRect().inflate(size.width)
                    ) {
                        this@drawWithContent.drawContent()
                        val density = Density(density)
                        drawPath(
                            path = Path().apply {
                                addOutline(
                                    centerShape.createOutline(
                                        size,
                                        layoutDirection,
                                        density
                                    )
                                )
                            },
                            color = Black,
                            blendMode = BlendMode.DstOut
                        )
                    }
                }
                .dropShadow(shape = centerShape) {
                    radius = when {
                        isPressed -> 40f
                        isHovered -> 70f
                        else -> 40f
                    }
                    spread = 15f
                    color = when {
                        isPressed -> swatch.v100
                        else -> swatch.v200.copy(alpha = .9f)
                    }
                }
        )
    }

    if (showTime) Row(
        modifier = Modifier
            .offset(x = (-16).dp)
            .align(Alignment.Center),
    ) {
        Text(
            text = if (timer.hours > 0) "${timer.hours}h" else "",
            modifier = Modifier
                .defaultMinSize(minWidth = 32.dp),
            color = swatch.v50,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            textAlign = TextAlign.Start,
            maxLines = 1,
        )
        TimelyTime(
            left = timer.minutes,
            right = timer.seconds,
            color = swatch.v50,
            fontSize = 56.sp,
            strokeWidth = 3.dp,
        )
    }
}

internal object PlayTriangle : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(size.width, size.height * .5f)
        path.lineTo(0f, size.height)
        path.close()
        path.transform(Matrix().apply {
            scale(x = .8f)
            translate(x = size.width * .25f)
        })
        return Outline.Generic(path = path)
    }

}

internal object PauseShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val barSize = Size(size.width * .4f, size.height)
        path.addRect(
            rect = Rect(
                offset = Offset(0f, 0f),
                size = barSize
            )
        )
        path.addRect(
            rect = Rect(
                offset = Offset(size.width * .6f, 0f),
                size = barSize
            )
        )
        return Outline.Generic(path = path)
    }

}

private fun lerpStep(start: Float, interval: Float, fraction: Float) = start + (interval * fraction)

private fun DrawScope.drawMinute(
    index: Int,
    measurer: TextMeasurer,
    absoluteDegree: Float,
    secInfluence: Float,
    secDegree: Float,
) {
    val targetDegree = (index + 1) * 90f

    val delta = min(
        ((absoluteDegree % 360f) - targetDegree).absoluteValue,
        ((absoluteDegree % 360f) - (targetDegree % 360f)).absoluteValue,
    )

    val thumbPush = when {
        delta < 15f -> 1f - (delta / 15f)
        else -> 0f
    }

    val secDelta = ((targetDegree % 360f) - secDegree + 180f).mod(360f) - 180f
    val secPush = 1f - (secDelta.absoluteValue / 18f).coerceIn(0f..1f)

    val push = lerp(thumbPush, secPush, secInfluence)
    val radiusMult = lerp(.38f, .34f, push)

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
                fontSize = 24.sp,
                shadow = Shadow(
                    color = Black.copy(alpha = .4f),
                    blurRadius = 10f,
                )
            )
        )
        drawText(
            textLayoutResult = result,
            topLeft = Offset(
                -result.size.width / 2f,
                -result.size.height / 2f,
            )
        )
    }
}