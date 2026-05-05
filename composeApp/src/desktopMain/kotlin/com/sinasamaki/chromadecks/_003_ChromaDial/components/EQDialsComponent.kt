package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Neutral800
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val eqBands = listOf(
    "Bass"   to 180f,
    "Mid"    to 135f,
    "Treble" to  90f,
)

@Composable
fun EQDials(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            eqBands.forEach { (label, initial) ->
                var degree by remember { mutableFloatStateOf(initial) }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
                ) {
                    Dial(
                        degree = degree,
                        onDegreeChange = { degree = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        startDegrees = 225f,
                        sweepDegrees = 270f,
                        thumb = { Box(Modifier.fillMaxSize()) },
                        track = { state ->
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .drawBehind {
                                        val r = size.minDimension / 2f
                                        val sw = 3.dp.toPx()
                                        val inset = sw / 2f + 1f
                                        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                                        val arcTopLeft = Offset(inset, inset)

                                        // Range arc
                                        drawArc(
                                            color = Neutral700,
                                            startAngle = 135f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            topLeft = arcTopLeft,
                                            size = arcSize,
                                            style = Stroke(width = sw, cap = StrokeCap.Round),
                                        )

                                        // Active arc
                                        val activeSweep = state.value * 270f
                                        if (activeSweep > 0f) {
                                            drawArc(
                                                color = Lime400,
                                                startAngle = 135f,
                                                sweepAngle = activeSweep,
                                                useCenter = false,
                                                topLeft = arcTopLeft,
                                                size = arcSize,
                                                style = Stroke(width = sw, cap = StrokeCap.Round),
                                            )
                                        }

                                        // Knob body
                                        drawCircle(color = Neutral800, radius = r * 0.72f)

                                        // Indicator line
                                        val rad = (state.absoluteDegree - 90f) * PI.toFloat() / 180f
                                        val tip = Offset(
                                            center.x + cos(rad) * r * 0.56f,
                                            center.y + sin(rad) * r * 0.56f,
                                        )
                                        drawLine(
                                            color = White,
                                            start = center,
                                            end = tip,
                                            strokeWidth = 2.5f,
                                            cap = StrokeCap.Round,
                                        )
                                        drawCircle(color = Lime400, radius = 2.5f, center = tip)
                                    }
                            )
                        }
                    )
                    Text(
                        text = label,
                        color = Zinc400,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
        }
    }
}
