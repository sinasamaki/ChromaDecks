package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.DialLayout
import com.sinasamaki.chroma.dial.RadiusMode
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay

@Composable
fun CameraFocusDial(modifier: Modifier = Modifier) {
    val animatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            animatable.snapTo(180f)
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
            delay(600)
        }
    }

    Box(
        modifier = modifier
            .background(Zinc900, RoundedCornerShape(50))
            .border(
                width = 0.dp,
                shape = CircleShape,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Lime500.copy(alpha = .5f),
                        Lime500.copy(alpha = .1f),
                    )
                ),
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CameraNeedleDial(
                degree = animatable.value,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1 / 2f),
                clockwise = false,
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "ANALOG",
                    fontSize = 7.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Zinc400,
                )
                Text(
                    text = "DISPLAY",
                    fontSize = 4.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Zinc400,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.1f)
                        .drawBehind {
                            drawRect(
                                color = Lime400,
                                size = androidx.compose.ui.geometry.Size(size.width, 4f)
                            )
                        }
                )
            }

            CameraNeedleDial(
                degree = animatable.value,
                modifier = Modifier.weight(1f).aspectRatio(1 / 2f),
                clockwise = true,
            )
        }
    }
}

@Composable
private fun CameraNeedleDial(
    degree: Float,
    modifier: Modifier = Modifier,
    clockwise: Boolean,
) {
    Dial(
        degree = degree,
        onDegreeChange = { },
        modifier = modifier,
        startDegrees = 0f,
        sweepDegrees = 180f,
        clockwise = clockwise,
        layout = DialLayout(
            radiusMode = RadiusMode.HEIGHT,
            center = Offset(if (clockwise) 0f else 1f, .5f)
        ),
        thumb = {
            Box(
                Modifier
                    .fillMaxWidth(.8f)
                    .aspectRatio(1f)
            )
        },
        track = { state ->
            val measure = rememberTextMeasurer()
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val r = size.width
                        val center = Offset(
                            x = if (clockwise) 0f else 1f * size.width,
                            y = center.y
                        )

                        clipRect { }

                        drawArc(
                            color = Lime200.copy(alpha = .1f),
                            startAngle = state.startDegrees,
                            sweepAngle = 180f * if (clockwise) 1f else -1f,
                            radius = r - 4.dp.toPx(),
                            center = center,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Butt
                        )

                        drawEveryInterval(
                            startDegrees = state.startDegrees,
                            sweepDegrees = 180f * if (clockwise) 1f else -1f,
                            interval = 4f,
                            radius = r,
                            center = center
                        ) { data ->
                            val isMajor = data.index % 5 == 0
                            val lineCenter = center - Offset(0f, this.center.y - 4.dp.toPx())
                            rotate(
                                degrees = data.rotationAngle * if (clockwise) 1f else -1f,
                                pivot = center,
                            ) {
                                if (isMajor) {
                                    drawLine(
                                        color = Zinc900,
                                        start = lineCenter - Offset(0f, 4.dp.toPx()),
                                        end = lineCenter + Offset(0f, 4.dp.toPx()),
                                        strokeWidth = 12f,
                                        cap = StrokeCap.Round,
                                    )

                                    drawLine(
                                        color = Lime500,
                                        start = lineCenter - Offset(0f, 4.dp.toPx()),
                                        end = lineCenter + Offset(0f, 4.dp.toPx()),
                                        strokeWidth = 4f,
                                        cap = StrokeCap.Round,
                                    )

                                    drawText(
                                        textMeasurer = measure,
                                        text = "${data.index}",
                                        topLeft = lineCenter + Offset(5.sp.toPx() * -1.5f, 20f),
                                        style = TextStyle(
                                            color = Zinc600,
                                            fontSize = 6.sp,
                                            textAlign = TextAlign.Center,
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        size = Size(6.sp.toPx() * 3, 6.sp.toPx())
                                    )
                                }
                            }
                        }

                        rotate(
                            degrees = (if (clockwise) 1f else -1f) * state.degree + state.overshootDegrees,
                            pivot = center,
                        ) {
                            drawLine(
                                color = Lime400,
                                start = Offset(if (clockwise) 0f else size.width, center.y * .35f),
                                end = Offset(if (clockwise) 0f else size.width, center.y),
                                strokeWidth = 1f,
                                cap = StrokeCap.Round,
                            )
                        }
                        drawCircle(
                            color = Lime400,
                            radius = 4f,
                            center = center,
                        )
                        drawCircle(
                            color = Black,
                            radius = 2.5f,
                            center = center,
                        )
                    }
            )
        }
    )
}
