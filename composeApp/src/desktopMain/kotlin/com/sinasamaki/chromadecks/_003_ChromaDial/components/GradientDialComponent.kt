package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chromadecks.ui.theme.Blue600
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Zinc950

@Composable
fun GradientDial() {
    var degree by remember { mutableFloatStateOf(0f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.fillMaxSize(),
            startDegrees = 0f,
            sweepDegrees = 360f,
            thumb = {
                Box(Modifier.size(56.dp))
            },
            track = { state ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationZ = state.degree - 90f + state.overshootDegrees
                        }
                        .drawBehind {
                            val strokeWidth = 16.dp.toPx()
                            val trackRadius = state.radius - strokeWidth

                            val colors = listOf(
                                Zinc950.copy(alpha = 1f),
                                Blue600,
                                Violet400,
                            )

                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = colors,
                                    center = center
                                ),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                topLeft = Offset(
                                    center.x - trackRadius,
                                    center.y - trackRadius
                                ),
                                size = Size(trackRadius * 2, trackRadius * 2),
                                useCenter = false,
                            )

                            val path = Path()
                            path.addArc(
                                oval = Rect(center, state.radius - (strokeWidth / 1)),
                                startAngleDegrees = 12f,
                                sweepAngleDegrees = 337f,
                            )
                            drawPath(
                                path,
                                brush = Brush.sweepGradient(
                                    colors = colors,
                                    center = center
                                ),
                                style = Stroke(
                                    width = strokeWidth * 2,
                                    cap = StrokeCap.Round,
                                )
                            )
                        }
                )
            }
        )
}
