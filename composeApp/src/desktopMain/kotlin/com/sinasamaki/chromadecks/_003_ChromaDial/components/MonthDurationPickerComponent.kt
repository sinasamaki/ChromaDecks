package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.TubeShape
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Neutral300
import com.sinasamaki.chromadecks.ui.theme.Neutral500
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Sky200
import com.sinasamaki.chromadecks.ui.theme.Sky300
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Blue600
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlin.math.roundToInt

@Composable
fun MonthDurationPicker() {
    val background = Zinc800
    val track = Zinc900
    val trackEdge = Neutral950
    val textColor = Neutral300

    var degree by remember { mutableStateOf(90f) }
    val animatedDegree by animateFloatAsState(targetValue = degree)
    Dial(
        degree = animatedDegree,
        onDegreeChange = { degree = it },
        sweepDegrees = 330f,
        startDegrees = 30f,
        interval = 30f,
        modifier = Modifier.fillMaxSize(),
        valueRange = 1f..12f,
        overshootDecay = .8f,
        overshootAnimationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        thumb = {
            Box(
                Modifier
                    .size(56.dp)
                    .padding(8.dp)
                    .graphicsLayer {
                        rotationZ = -it.absoluteDegree
                    }
                    .dropShadow(shape = CircleShape) {
                        radius = 10f
                        alpha = .4f
                    }
                    .border(
                        width = 2.dp,
                        shape = CircleShape,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Sky200.copy(alpha = .3f),
                                track.copy(alpha = .8f),
                            )
                        )
                    )
                    .background(background, CircleShape)
            )
        },
        track = { dialState ->
            BoxWithConstraints {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .dropShadow(shape = CircleShape) {
                            radius = 7f
                            alpha = .15f
                            offset = Offset(0f, -8f)
                            spread = 4f
                        }
                        .background(shape = CircleShape, color = track)
                        .drawBehind {
                            val ringStroke = 56.dp.toPx()
                            val ringRadius = center.x - (ringStroke / 2)

                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 330f,
                                radius = ringRadius,
                                spacing = 30f,
                            ) { data ->
                                drawCircle(
                                    color = Neutral500,
                                    radius = 3.dp.toPx(),
                                    center = data.position,
                                )
                            }
                        },
                )

                val density = LocalDensity.current
                val tubeRadius = with(density) { 56.dp.toPx() / 2 }
                val tubeCornerRadius = RoundedCornerShape(
                    topStart = 20f,
                    bottomStart = 20f,
                    topEnd = tubeRadius,
                    bottomEnd = tubeRadius,
                )

                val activeShape = remember(dialState.degree, dialState.overshootDegrees) {
                    TubeShape(
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 30f + dialState.degree + dialState.overshootDegrees,
                        tubeRadius = tubeRadius,
                        cornerRadius = tubeCornerRadius,
                    )
                }

                val donutShape = remember {
                    TubeShape(
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 360f,
                        tubeRadius = tubeRadius,
                        cornerRadius = tubeCornerRadius,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(donutShape)
                        .blur(
                            radius = 8.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded,
                        )
                        .border(
                            width = 4.dp,
                            shape = donutShape,
                            color = trackEdge
                        ),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(5f)
                        .clip(donutShape)
                        .blur(
                            radius = 40.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded,
                        )
                        .background(color = Sky500, shape = activeShape),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Sky500, shape = activeShape)
                        .background(
                            brush = Brush.radialGradient(
                                .7f to Blue600.copy(alpha = 0.6f),
                                1f to Transparent,
                            ),
                            shape = activeShape,
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(activeShape)
                        .blur(
                            radius = 20.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded,
                        )
                        .background(color = Sky300.copy(alpha = .2f), shape = activeShape)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(activeShape)
                        .blur(
                            radius = 4.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded,
                        )
                        .offset(x = (-3).dp, y = (-3).dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(Transparent, Zinc100)
                            ),
                            shape = activeShape,
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(56.dp)
                        .background(color = background, shape = CircleShape)
                        .innerShadow(shape = CircleShape) {
                            radius = 6f
                            offset = Offset(0f, -6f)
                            alpha = .2f
                            spread = 5f
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        0.dp,
                        alignment = Alignment.CenterVertically,
                    ),
                ) {
                    val monthIndex = dialState.mappedValue.roundToInt()
                    AnimatedContent(
                        targetState = monthIndex,
                        modifier = Modifier.fillMaxWidth(),
                        transitionSpec = {
                            slideInVertically(
                                initialOffsetY = {
                                    (
                                            if (targetState > initialState)
                                                it * .05f
                                            else
                                                -it * .05f
                                            ).roundToInt()
                                }
                            ) togetherWith fadeOut(snap())
                        }
                    ) {
                        Text(
                            text = "$it",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = textColor,
                            fontSize = 72.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = if (monthIndex == 1) "month" else "months",
                        color = textColor.copy(alpha = .5f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
        },
    )
}
