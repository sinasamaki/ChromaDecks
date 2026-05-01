package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime800
import com.sinasamaki.chromadecks.ui.util.toDp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal data class ChromaDialTeaserState(
    val showPoint: Boolean = false,
    val showTrack: Boolean = false,
    val showCode: Boolean = false,
)

internal class ChromaDialTeaserSlide : ListSlideAdvanced<ChromaDialTeaserState>() {

    override val initialState get() = ChromaDialTeaserState()

    override val stateMutations
        get() = listOf<ChromaDialTeaserState.() -> ChromaDialTeaserState>(
            { copy(showPoint = true) },
            { copy(showTrack = true) },
            { copy(showCode = true) },
        )

    @Composable
    override fun content(state: ChromaDialTeaserState) {
        var width by remember { mutableStateOf(0) }
        var rawOffset by remember { mutableStateOf(Offset(0f, -100f)) }
        var startOffset by remember { mutableStateOf(Offset(0f, -100f)) }
        var isDragging by remember { mutableStateOf(false) }
        val textMeasurer = rememberTextMeasurer()

        val degrees = atan2(rawOffset.y, rawOffset.x) * (180f / PI.toFloat())
        val offsetComment = "// Offset(x=%.1f, y=%.1f)".format(rawOffset.x, rawOffset.y)
        val degreesComment = "// %.1f°".format(degrees)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(96.dp)
                .onSizeChanged { width = it.width },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = state.showCode,
                modifier = Modifier,
                enter = expandHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioLowBouncy,
                    )
                ),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start),
            ) {
                LaunchedEffect(Unit) { rawOffset = Offset.Zero }
                Box(
                    modifier = Modifier
                        .width((width / 2).toDp())
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    CodeIDE(
                        tabs = listOf(
                            "DialAngle.kt" to """
var offset by remember {
    mutableStateOf(Offset.Zero)
}
$offsetComment

val degrees = atan2(
    y = offset.y,
    x = offset.x,
) * (180f / PI.toFloat())
$degreesComment
                            """.trimIndent(),
                        ),
                        selectedTab = 0,
                        onTabSelect = {},
                        fadeAnimations = false,
                        enableAnimations = true,
                        highlightAnimation = rawOffset == Offset.Zero,
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                val thumbSizeDp = 32.dp

                val pointRadiusFraction by animateFloatAsState(
                    targetValue = if (state.showPoint) 1f else 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium,
                    )
                )

                val pointSlideOffset by animateFloatAsState(
                    targetValue = if (state.showPoint) 0f else 40.dp.value,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )

                val trackRadiusFraction by animateFloatAsState(
                    targetValue = if (state.showTrack) 1f else 0f,
                    animationSpec = if (state.showTrack) {
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        )
                    } else {
                        spring(
                            stiffness = Spring.StiffnessLow,
                        )
                    }
                )

                Canvas(
                    modifier = Modifier
                        .size(420.dp)
                        .pointerInput(state.showCode) {
                            if (!state.showCode) return@pointerInput
                            detectDragGestures(
                                onDragStart = { startPos ->
                                    isDragging = true
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    rawOffset = startPos - center
                                    startOffset = rawOffset
                                },
                                onDragEnd = { isDragging = false },
                                onDragCancel = { isDragging = false },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    rawOffset += dragAmount
                                },
                            )
                        }
                ) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val thumbSizePx = thumbSizeDp.toPx()
                    val maxRadius = size.minDimension / 2f - thumbSizePx / 2f - 40.dp.toPx()
                    val currentRadius = maxRadius * trackRadiusFraction

                    if (pointRadiusFraction > 0f) {
                        drawCircle(
                            color = Lime400,
                            radius = 6.dp.toPx() * pointRadiusFraction,
                            center = center + Offset(0f, pointSlideOffset.dp.toPx()),
                        )
                    }

                    if (currentRadius > 0f) {
                        drawCircle(
                            color = Lime800,
                            radius = currentRadius,
                            center = center,
                            style = Stroke(width = 4.dp.toPx()),
                        )
                    }

                    if (state.showCode) {
                        val len = sqrt(rawOffset.x * rawOffset.x + rawOffset.y * rawOffset.y)
                        val direction = if (len > 0f) rawOffset / len else Offset(0f, -1f)
                        val thumbCenter = center + direction * maxRadius

                        drawCircle(
                            color = Lime400,
                            radius = thumbSizePx / 2f,
                            center = thumbCenter,
                            style = Stroke(width = 2.dp.toPx()),
                        )

                        if (isDragging) {
                            val touchPos = center + rawOffset

                            // Touch indicator
                            drawCircle(
                                color = Lime400.copy(alpha = 0.6f),
                                radius = thumbSizePx / 2f,
                                center = touchPos,
                            )

                            // Angle indicator
                            val startDist =
                                sqrt(startOffset.x * startOffset.x + startOffset.y * startOffset.y)
                            val currentDist =
                                sqrt(rawOffset.x * rawOffset.x + rawOffset.y * rawOffset.y)
                            if (startDist > 5f && currentDist > 5f) {
                                val startAngleRad = atan2(startOffset.y, startOffset.x)
                                val currentAngleRad = atan2(rawOffset.y, rawOffset.x)
                                val startAngleDeg = startAngleRad * (180f / PI.toFloat())
                                val currentAngleDeg = currentAngleRad * (180f / PI.toFloat())
                                var sweepAngle = currentAngleDeg - startAngleDeg
                                while (sweepAngle > 180f) sweepAngle -= 360f
                                while (sweepAngle < -180f) sweepAngle += 360f

                                val startDir = Offset(cos(startAngleRad), sin(startAngleRad))
                                val currentDir = Offset(cos(currentAngleRad), sin(currentAngleRad))

                                // Start angle line (dashed)
                                drawLine(
                                    color = Lime400.copy(alpha = 0.4f),
                                    start = center,
                                    end = center + startDir * maxRadius,
                                    strokeWidth = 1.5.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(8f, 8f),
                                        0f
                                    ),
                                )

                                // Current angle line
                                drawLine(
                                    color = Lime400.copy(alpha = 0.8f),
                                    start = center,
                                    end = center + currentDir * maxRadius,
                                    strokeWidth = 1.5.dp.toPx(),
                                )

                                // Arc between angles
                                val arcRadius = 50f
                                drawArc(
                                    color = Lime400.copy(alpha = 0.35f),
                                    startAngle = startAngleDeg,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
                                    size = Size(arcRadius * 2, arcRadius * 2),
                                    style = Stroke(width = 1.5.dp.toPx()),
                                )

                                // Degree text
                                val degreeText = "%.0f°".format(abs(sweepAngle))
                                val degreeTextStyle = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 18.sp,
                                    color = Lime400,
                                )
                                val degreeLayout = textMeasurer.measure(degreeText, degreeTextStyle)

                                val midAngleRad =
                                    startAngleRad + (sweepAngle / 2f) * (PI.toFloat() / 180f)
                                val textPos = center + Offset(
                                    cos(midAngleRad),
                                    sin(midAngleRad)
                                ) * (arcRadius + 48f)

                                drawText(
                                    textLayoutResult = degreeLayout,
                                    color = Lime400,
                                    topLeft = textPos - Offset(
                                        degreeLayout.size.width / 2f,
                                        degreeLayout.size.height / 2f,
                                    ),
                                )
                            }

                            // Coordinates text at top of canvas
                            val coordText = "x: %.1f  y: %.1f".format(rawOffset.x, rawOffset.y)
                            val coordStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 18.sp,
                                color = Lime400,
                            )
                            val coordLayout = textMeasurer.measure(coordText, coordStyle)
                            val textTopY = 4f
                            val textTopX = center.x - coordLayout.size.width / 2f

                            drawText(
                                textLayoutResult = coordLayout,
                                color = Lime400,
                                topLeft = Offset(textTopX, textTopY),
                            )

                            // Leader line: down from text, then bend to touch position
                            val lineStartY = textTopY + coordLayout.size.height + 6f
                            val bendY = lineStartY + 28f
                            drawPath(
                                path = Path().apply {
                                    moveTo(center.x, lineStartY)
                                    lineTo(center.x, bendY)
                                    lineTo(touchPos.x, touchPos.y)
                                },
                                color = Lime400.copy(alpha = 0.45f),
                                style = Stroke(width = 1.dp.toPx()),
                            )
                        }
                    }
                }
            }
        }
    }
}
