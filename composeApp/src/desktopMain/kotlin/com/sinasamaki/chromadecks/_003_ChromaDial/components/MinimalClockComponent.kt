package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.DialInterval
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc100

@Composable
fun MinimalClock() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var minute by remember { mutableStateOf(0f) }
        Dial(
            degree = minute,
            onDegreeChange = { minute = it },
            modifier = Modifier.size(300.dp),
            thumb = {
                Box(Modifier.size(48.dp))
            },
            track = { dialState ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .dropShadow(shape = CircleShape) {
                            radius = 40f
                            alpha = .2f
                        }
                        .background(color = Zinc100, shape = CircleShape)
                        .drawWithContent {
                            val path = Path()
                            path.moveTo(center.x, center.y)
                            path.relativeLineTo(-60f, -center.y - 10f)
                            path.relativeLineTo(120f, 0f)
                            path.close()

                            val matrix = Matrix()
                            matrix.rotateZ(dialState.degree + dialState.overshootDegrees)
                            matrix.translate(-center.x, -center.y)
                            path.transform(matrix)
                            path.transform(Matrix().apply { translate(center.x, center.y) })

                            drawCircle(
                                color = White,
                                radius = center.x - 1.dp.toPx(),
                                style = Stroke(width = 2.dp.toPx())
                            )

                            clipPath(path = path) {
                                this@drawWithContent.drawContent()
                                drawCircle(
                                    color = Lime200,
                                    radius = center.x - 1.dp.toPx(),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        }
                ) {
                    DialInterval(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Lime500, shape = CircleShape),
                        sweepDegrees = 360f,
                        spacing = 30f,
                    ) { data ->
                        val showNumber by remember {
                            derivedStateOf {
                                (dialState.degree < 180f && data.index != 12) ||
                                        (dialState.degree > 180f && data.index != 0)
                            }
                        }
                        if (showNumber)
                            Text(
                                text = "${data.index * 5}".padStart(2, '0'),
                                modifier = Modifier
                                    .rotate(-data.rotationAngle - 90f)
                                    .padding(12.dp),
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                    }
                }
            }
        )

        var hour by remember { mutableStateOf(0f) }
        val animatedHour by animateFloatAsState(targetValue = hour)

        Dial(
            degree = animatedHour,
            onDegreeChange = { hour = it },
            modifier = Modifier.size(200.dp),
            thumb = {
                Box(Modifier.size(48.dp))
            },
            interval = 30f,
            track = { dialState ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .dropShadow(shape = CircleShape) {
                            radius = 20f
                            alpha = .2f
                        }
                        .background(color = Zinc100, shape = CircleShape)
                        .drawWithContent {
                            val path = Path()
                            path.moveTo(center.x, center.y)
                            path.relativeLineTo(-60f, -center.y - 10f)
                            path.relativeLineTo(120f, 0f)
                            path.close()

                            val matrix = Matrix()
                            matrix.rotateZ(dialState.degree + dialState.overshootDegrees)
                            matrix.translate(-center.x, -center.y)
                            path.transform(matrix)
                            path.transform(Matrix().apply { translate(center.x, center.y) })

                            drawCircle(
                                color = White,
                                radius = center.x - 1.dp.toPx(),
                                style = Stroke(width = 2.dp.toPx())
                            )

                            clipPath(path = path) {
                                this@drawWithContent.drawContent()
                                drawCircle(
                                    color = Lime200,
                                    radius = center.x - 1.dp.toPx(),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        }
                ) {
                    DialInterval(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Lime500, shape = CircleShape),
                        sweepDegrees = 360f,
                        spacing = 30f,
                    ) { data ->
                        if (data.index != 0)
                            Text(
                                text = "${data.index}",
                                modifier = Modifier
                                    .rotate(-data.rotationAngle - 90f)
                                    .padding(12.dp),
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color = Zinc100, shape = CircleShape)
        )
    }
}
