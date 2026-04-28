package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.createTubePath
import com.sinasamaki.chromadecks.ui.theme.Blue200
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

internal fun polarOffset(center: Offset, radius: Float, degrees: Float): Offset {
    val rad = degrees * (PI / 180.0)
    return Offset(
        center.x + radius * cos(rad).toFloat(),
        center.y + radius * sin(rad).toFloat(),
    )
}

@Composable
fun TemperatureDial() {
    var degree by remember { mutableFloatStateOf(90f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.fillMaxSize(),
            startDegrees = 225f,
            sweepDegrees = 270f,
            thumb = { state ->
                val temperature = (10 + state.value * 30).roundToInt()
                val color by animateColorAsState(
                    targetValue = when {
                        temperature > 30 -> Red600
                        temperature > 25 -> Orange400
                        temperature > 18 -> Yellow400
                        else -> Lime500
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                )
                Box(
                    Modifier
                        .size(40.dp)
                        .background(color = White, shape = CircleShape)
                        .padding(6.dp)
                        .background(color = color, shape = CircleShape)
                )
            },
            track = { state ->
                val temperature = (10 + state.value * 30).roundToInt()
                val color by animateColorAsState(
                    targetValue = when {
                        temperature > 37 -> Red600
                        temperature > 30 -> Red500
                        temperature > 25 -> Orange400
                        temperature > 18 -> Yellow400
                        else -> Lime500
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                )

                BoxWithConstraints {
                    val density = LocalDensity.current
                    val strokePx = with(density) { 20.dp.toPx() }
                    val bwCenter = Offset(
                        with(density) { maxWidth.toPx() } / 2f,
                        with(density) { maxHeight.toPx() } / 2f
                    )
                    val overshoot = state.overshootDegrees
                    val startAngle = state.startDegrees + minOf(0f, overshoot)
                    val activeSweep =
                        (state.degree - state.degreeRange.start).coerceAtLeast(1f) +
                                kotlin.math.abs(overshoot)

                    val path = remember(bwCenter, state.radius, strokePx, startAngle, activeSweep) {
                        createTubePath(
                            center = bwCenter,
                            radius = state.radius - strokePx,
                            startAngleDegrees = startAngle,
                            sweepAngleDegrees = activeSweep,
                            tubeRadius = strokePx / 2,
                            density = density,
                        )
                    }

                    val pathShape = remember(path, constraints) {
                        object : Shape {
                            override fun createOutline(
                                size: Size,
                                layoutDirection: LayoutDirection,
                                density: Density
                            ) = Outline.Generic(path)
                        }
                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawPath(
                                    path = path,
                                    color = Blue200,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                            .clip(pathShape)
                            .blur(radius = 6.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .drawBehind {
                                drawPath(
                                    path = path,
                                    color = Blue500,
                                    style = Stroke(width = 6.dp.toPx())
                                )
                            }
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .blur(radius = 40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .drawBehind {
                                drawCircle(
                                    color = color,
                                    radius = 24.dp.toPx(),
                                    center = polarOffset(
                                        center = this.center,
                                        radius = state.radius - strokePx,
                                        degrees = state.absoluteDegree - 90f + overshoot
                                    )
                                )
                            }
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .blur(radius = 60.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .drawBehind {
                                inset(inset = strokePx) {
                                    drawArc(
                                        color = color,
                                        startAngle = state.absoluteDegree - 90f + overshoot,
                                        sweepAngle = -activeSweep * .4f,
                                        useCenter = false,
                                        style = Stroke(width = strokePx * 1.5f)
                                    )
                                }
                            }
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .clip(pathShape)
                            .blur(radius = 40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .drawBehind {
                                inset(inset = strokePx) {
                                    drawArc(
                                        color = color,
                                        startAngle = state.absoluteDegree - 90f + overshoot,
                                        sweepAngle = -activeSweep * .5f,
                                        useCenter = false,
                                        style = Stroke(width = strokePx * 1.5f)
                                    )
                                }
                            }
                    )

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "$temperature°C",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            ),
                            color = White,
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Living room",
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            White.copy(alpha = 0f),
                                            White.copy(alpha = .2f),
                                        )
                                    ),
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 1.dp,
                                    shape = CircleShape,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            White,
                                            White.copy(alpha = .1f),
                                        )
                                    ),
                                )
                                .padding(vertical = 4.dp, horizontal = 10.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                            ),
                            color = White,
                        )
                    }
                }
            }
        )
}
