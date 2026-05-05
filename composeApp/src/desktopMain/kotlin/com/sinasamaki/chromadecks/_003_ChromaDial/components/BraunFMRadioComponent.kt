package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Neutral800
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlin.math.sqrt

private val fmFrequencies = listOf(0, 150, 24, 110, 22, 90, 20, 75, 70, 18, 65, 17, 60, 16, 55, 15)

@Composable
fun BraunFMRadio(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .aspectRatio(1 / 2f)
            .fillMaxHeight()
            .scale(1.2f)
    ) {
        // Dot-grid speaker grill
        Canvas(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            val cols = 11
            val rows = 9
            val sx = size.width / cols
            val sy = size.height / rows
            val dotR = minOf(sx, sy) * 0.2f
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    drawCircle(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc600,
                                Black,
                            ),
                            startY = row * sy,
                            endY = row * sy + sy,
                        ),
                        radius = dotR * 1.6f,
                        center = Offset(col * sx + sx / 2f, row * sy + sy / 2f),
                    )
                    drawCircle(
                        color = Zinc400,
                        radius = dotR,
                        center = Offset(col * sx + sx / 2f, row * sy + sy / 2f),
                    )
                    drawCircle(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc700,
                                White,
                            ),
                            startY = row * sy,
                            endY = row * sy + sy,
                        ),
                        radius = dotR,
                        center = Offset(col * sx + sx / 2f, row * sy + sy / 2f),
                        style = Stroke(width = 1f)
                    )
                }
            }
        }

        // FM tuner dial
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f),
            contentAlignment = Alignment.Center,
        ) {
            var degree by remember { mutableFloatStateOf(0f) }
            val textMeasurer = rememberTextMeasurer()
            val labelStyle = TextStyle(
                fontSize = 7.sp,
                fontFamily = FontFamily.Monospace,
                color = White.copy(alpha = 0.85f),
            )

            Dial(
                degree = degree,
                onDegreeChange = { degree = it },
                modifier = Modifier
                    .fillMaxHeight(0.88f)
                    .aspectRatio(1f),
                thumb = { Box(Modifier.fillMaxSize()) },
                track = { state ->
                    Box(Modifier.fillMaxSize()) {

                        // Static outer bezel
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .drawBehind {
                                    val r = size.minDimension / 2f
                                    drawCircle(color = Neutral800)
                                    drawCircle(color = Neutral700, style = Stroke(width = 1.5f))
                                }
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .graphicsLayer { rotationZ = state.degree + state.overshootDegrees }
                                .drawBehind {
                                    val r = size.minDimension / 2f

//                                    val r = size.minDimension / 2f
                                    drawCircle(color = Neutral900, radius = r * 0.97f)

                                    // Rectangular grip handles at 120° and 240°
                                    for (angleDeg in listOf(120f, 240f)) {
                                        rotate(angleDeg, Offset(center.x, center.y)) {
                                            val hx = center.x
                                            val hy = center.y - r * 0.73f
                                            drawRoundRect(
                                                color = Neutral700,
                                                topLeft = Offset(hx - 4f, hy - 14f),
                                                size = Size(8f, 28f),
                                                cornerRadius = CornerRadius(
                                                    5f
                                                ),
                                            )
                                        }
                                    }

                                    val tipY = center.y - r * 0.83f
                                    val path = Path().apply {
                                        moveTo(center.x, tipY)
                                        lineTo(center.x - r * 0.045f, tipY + r * 0.09f)
                                        lineTo(center.x + r * 0.045f, tipY + r * 0.09f)
                                        close()
                                    }
                                    drawPath(path, White)
                                }
                        )

                        // Rotating frequency labels
                        Canvas(Modifier.fillMaxSize()) {
                            val r = size.minDimension / 2f
                            val totalRotation = 0f//state.degree + state.overshootDegrees

                            rotate(degrees = totalRotation, pivot = center) {
                                drawEveryInterval(
                                    startDegrees = 0f,
                                    sweepDegrees = 360f,
                                    interval = 22.5f,
                                    radius = r,
                                ) { data ->
                                    rotate(data.rotationAngle, pivot = data.position) {
                                        val text = fmFrequencies.getOrElse(data.index) { 0 }
                                        val layout = textMeasurer.measure("$text", labelStyle)
                                        drawText(
                                            textLayoutResult = layout,
                                            color = if (text < 25 && text != 0) Lime500 else Zinc200,
                                            topLeft = Offset(
                                                data.position.x - layout.size.width / 2f,
                                                data.position.y - layout.size.height / 2f,
                                            ),
                                        )
                                    }
                                }
                            }
                        }

                        Canvas(
                            Modifier
                                .fillMaxSize()
                                .rotate(state.degree + state.overshootDegrees)
                        ) {
                            val r = size.minDimension / 2f
                            drawCircle(
                                color = Neutral700,
                                radius = r * 0.22f,
                                style = Stroke(width = 1.5f)
                            )
                            drawCircle(
                                White,
                                radius = 3f,
                                center = Offset(center.x, center.y - r * 0.09f)
                            )
                            drawCircle(
                                White,
                                radius = 3f,
                                center = Offset(center.x, center.y + r * 0.09f)
                            )
                        }
                    }
                }
            )
        }
    }
}
