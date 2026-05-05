package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime700
import com.sinasamaki.chromadecks.ui.theme.Lime800


private val rotaryNumbers = listOf("0", "9", "8", "7", "6", "5", "4", "3", "2", "1")

/**
 * Hello fellow git traveller 👋
 * If you are looking for this component, know that it is not functional,
 * and is only for display purposes
 * If you would like me to make it work, please comment
 * and ask on my video, plus a dolphin emoji 🐬✨
 */

@Composable
fun RotaryDial(modifier: Modifier = Modifier) {
    var degree by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()

    LaunchedEffect(isDragging) {
        if (!isDragging) {
            val anim = Animatable(degree)
            anim.animateTo(
                targetValue = -500f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                ),
            ) { degree = value.coerceAtLeast(0f) }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val numberStyle = TextStyle(
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        color = Lime300,
    )

    var absoluteDegree by remember { mutableStateOf(0f) }

    val animatedDegree by animateFloatAsState(degree)
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Dial(
            degree = animatedDegree,
            onDegreeChange = {
                degree = it
            },
            modifier = Modifier.fillMaxSize(),
            sweepDegrees = 300f,
            interval = 30f,
            interactionSource = interactionSource,
            thumb = { Box(Modifier.fillMaxSize()) },
            track = { state ->
                absoluteDegree = state.absoluteDegree

                Box(Modifier.fillMaxSize()) {


                    Canvas(Modifier.fillMaxSize()) {
                        val r = size.minDimension / 2f
                        val holeRadius = r * 0.115f
                        val totalRotation = state.degree + state.overshootDegrees

                        drawCircle(
                            color = Lime400,
                            style = Stroke(width = 1f)
                        )

                        drawCircle(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Lime400.copy(alpha = .1f),
                                    Lime400.copy(alpha = .2f),
                                    Lime400.copy(alpha = .1f),
                                )
                            ),
                            style = Stroke(width = (holeRadius * 2) + 20.dp.toPx()),
                            radius = center.x - ((holeRadius * 2) + 20.dp.toPx()) / 2
                        )

                        drawCircle(
                            color = Lime400,
                            style = Stroke(width = 1f),
                            radius = center.x - holeRadius * 2 - 20.dp.toPx(),
                        )

                        drawLine(
                            color = Lime500,
                            start = Offset(center.x + r * 0.72f, center.y),
                            end = Offset(center.x + r * 0.93f, center.y),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round,
                        )

                        rotate(degrees = totalRotation, pivot = center) {
                            drawEveryInterval(
                                startDegrees = 105f,
                                sweepDegrees = 270f,
                                interval = 30f,
                                radius = r - holeRadius - 10.dp.toPx(),
                            ) { data ->
                                drawCircle(Black, radius = holeRadius, center = data.position)
                                drawCircle(
                                    color = Lime500.copy(alpha = .5f),
                                    radius = holeRadius,
                                    center = data.position,
                                    style = Stroke(width = 1.5f),
                                )

                                rotate(-totalRotation, pivot = data.position) {
                                    val text = rotaryNumbers.getOrElse(data.index) { "" }
                                    val layout = textMeasurer.measure(text, numberStyle)
                                    drawText(
                                        textLayoutResult = layout,
                                        topLeft = Offset(
                                            data.position.x - layout.size.width / 2f,
                                            data.position.y - layout.size.height / 2f,
                                        ),
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Black, RoundedCornerShape(4.dp))
                            .border(0.dp, Lime800, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "9114-12",
                            fontSize = 5.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Lime700,
                        )
                    }
                }
            }
        )
    }
}
