package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chromadecks.ui.theme.Blue
import com.sinasamaki.chromadecks.ui.theme.Emerald600
import com.sinasamaki.chromadecks.ui.theme.Lime500
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val accentSwatch = Blue
@Composable
fun MaterialDial() {
    var degree by remember { mutableFloatStateOf(90f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.fillMaxSize(),
            startDegrees = 180f,
            sweepDegrees = 275f,
            thumb = { state ->
                Box(
                    Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val showThumb by remember {
                        derivedStateOf {
                            state.degree < state.degreeRange.endInclusive
                        }
                    }
                    val scale by animateFloatAsState(
                        targetValue = if (showThumb) 1f else 0f
                    )
                    Box(
                        Modifier
                            .offset(y = (-16).dp)
                            .width(6.dp)
                            .height(40.dp)
                            .graphicsLayer { scaleY = scale }
                            .background(color = accentSwatch.v500, shape = CircleShape)
                    )
                }
            },
            track = { state ->
                val trackWidth = 16.dp
                Box(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val strokeWidth = trackWidth.toPx()
                            val trackRadius = state.radius - strokeWidth / 2
                            val overshoot = state.overshootDegrees
                            val startAngle = state.startDegrees - 90f + minOf(0f, overshoot)
                            val sweepRange =
                                state.degreeRange.endInclusive - state.degreeRange.start
                            val baseActiveSweep = (state.degree - state.degreeRange.start) - 10f
                            val activeSweep = baseActiveSweep + kotlin.math.abs(overshoot)
                            val inactiveSweep = sweepRange - baseActiveSweep - 20f
                            val inactiveStart = state.startDegrees - 90f + baseActiveSweep + 20f

                            if (inactiveSweep > 0f) {
                                drawArc(
                                    color = Emerald600.copy(alpha = .2f),
                                    startAngle = inactiveStart,
                                    sweepAngle = inactiveSweep,
                                    topLeft = Offset(
                                        center.x - trackRadius,
                                        center.y - trackRadius
                                    ),
                                    size = Size(trackRadius * 2, trackRadius * 2),
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            if (activeSweep > 0f) {
                                drawArc(
                                    color = Lime500,
                                    startAngle = startAngle,
                                    sweepAngle = activeSweep,
                                    topLeft = Offset(
                                        center.x - trackRadius,
                                        center.y - trackRadius
                                    ),
                                    size = Size(trackRadius * 2, trackRadius * 2),
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            val endAngleRadians =
                                (startAngle + sweepRange) * PI.toFloat() / 180f
                            val stopIndicatorX =
                                center.x + trackRadius * cos(endAngleRadians)
                            val stopIndicatorY =
                                center.y + trackRadius * sin(endAngleRadians)
                            drawCircle(
                                color = accentSwatch.v400,
                                radius = 4.dp.toPx(),
                                center = Offset(stopIndicatorX, stopIndicatorY)
                            )
                        }
                )
            }
        )
}
