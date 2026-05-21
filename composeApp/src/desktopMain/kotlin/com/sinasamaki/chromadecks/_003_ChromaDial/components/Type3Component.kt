package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.createTubePath
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Blue400
import com.sinasamaki.chromadecks.ui.theme.Emerald400
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime600
import com.sinasamaki.chromadecks.ui.theme.Lime700
import com.sinasamaki.chromadecks.ui.theme.Lime800
import com.sinasamaki.chromadecks.ui.theme.Lime900
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import kotlinx.coroutines.delay

private val accentColor = Sky400

private fun buildHandPath(
    width: Float,
    height: Float,
    cornerRadius: Float,
    topCornerRadius: Float,
): Path = Path().apply {
    val top = Offset(width / 2f, 0f)
    val br = Offset(width, height)
    val bl = Offset(0f, height)

    val topToBr = (br - top).let { it / it.getDistance() }
    val topToBl = (bl - top).let { it / it.getDistance() }
    val brToTop = (top - br).let { it / it.getDistance() }
    val brToBl = (bl - br).let { it / it.getDistance() }
    val blToBr = (br - bl).let { it / it.getDistance() }
    val blToTop = (top - bl).let { it / it.getDistance() }

    val topOut = top + topToBr * topCornerRadius
    val topIn = top + topToBl * topCornerRadius
    val brIn = br + brToTop * cornerRadius
    val brOut = br + brToBl * cornerRadius
    val blIn = bl + blToBr * cornerRadius
    val blOut = bl + blToTop * cornerRadius

    moveTo(topOut.x, topOut.y)
    lineTo(brIn.x, brIn.y)
    cubicTo(br.x, br.y, br.x, br.y, brOut.x, brOut.y)
    lineTo(blIn.x, blIn.y)
    cubicTo(bl.x, bl.y, bl.x, bl.y, blOut.x, blOut.y)
    lineTo(topIn.x, topIn.y)
    cubicTo(top.x, top.y, top.x, top.y, topOut.x, topOut.y)
}

@Composable
fun Type3() {
    val minuteAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            minuteAnimatable.snapTo(0f)
            minuteAnimatable.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
            delay(600)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Dial(
            degree = minuteAnimatable.value,
            onDegreeChange = { },
            sweepDegrees = 360f,
            startDegrees = 0f,
            modifier = Modifier.fillMaxSize(),
            thumb = {
                Box(Modifier.fillMaxSize(.4f))
            },
            overshootAnimationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = Spring.DampingRatioMediumBouncy,
            ),
            track = { minuteState ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawCircle(color = Neutral950)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    0f to Lime500.copy(alpha = 0f),
                                    .7f to Lime500.copy(alpha = 0f),
                                    1f to Lime500.copy(alpha = .1f),
                                )
                            )
                            drawCircle(
                                color = Lime800,
                                radius = center.x - 2.dp.toPx(),
                                style = Stroke(width = 1.dp.toPx()),
                            )

                            val tickRadius = center.x - 8.dp.toPx()
                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 354f,
                                radius = tickRadius,
                                interval = 6f,
                            ) { data ->
                                val isHour = data.index % 5 == 0
                                val tickLen = if (isHour) 12.dp.toPx() else 5.dp.toPx()
                                val strokeW = if (isHour) 2.dp.toPx() else 1.dp.toPx()
                                val color = if (isHour) Lime500 else Lime900

                                rotate(degrees = data.rotationAngle, pivot = data.position) {
                                    if (isHour) {
                                        drawCircle(
                                            color = accentColor,
                                            center = data.position + Offset(0f, 3.dp.toPx()),
                                            radius = 3.dp.toPx(),
                                            style = Stroke(width = 1.dp.toPx()),
                                        )
                                    } else {
                                        drawLine(
                                            color = color,
                                            start = data.position,
                                            end = data.position + Offset(0f, tickLen),
                                            strokeWidth = strokeW,
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = minuteState.degree + minuteState.overshootDegrees
                            }
                            .fillMaxSize()
                            .padding(32.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                Modifier
                                    .weight(4f)
                                    .width(20.dp)
                                    .drawBehind {
                                        inset(bottom = 10f, left = 0f, top = 0f, right = 0f) {
                                            drawPath(
                                                path = buildHandPath(
                                                    width = size.width,
                                                    height = size.height,
                                                    cornerRadius = 16.dp.toPx(),
                                                    topCornerRadius = 40.dp.toPx(),
                                                ),
                                                color = Lime400,
                                            )
                                        }
                                    }
                            )
                            HourDial(
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ =
                                            -(minuteState.degree + minuteState.overshootDegrees)
                                    }
                                    .weight(6f)
                                    .aspectRatio(1f)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(BiasAlignment(0f, -.3f))
                                .fillMaxHeight(.3f)
                                .fillMaxWidth()
                        ) {
                            SecondsDial(
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ =
                                            -(minuteState.degree + minuteState.overshootDegrees)
                                    }
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            )
                            Spacer(Modifier.weight(1f))
                            DayOfWeekDial(
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ =
                                            -(minuteState.degree + minuteState.overshootDegrees)
                                    }
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun HourDial(modifier: Modifier = Modifier) {
    var degree by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            degree = 0f
            for (i in 1..12) {
                degree = 30f * i
                delay(300)
            }
            delay(600)
        }
    }

    val animatedDegree by animateFloatAsState(
        targetValue = degree,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
        )
    )

    Dial(
        degree = animatedDegree,
        onDegreeChange = { },
        sweepDegrees = 360f,
        startDegrees = 0f,
        interval = 30f,
        modifier = modifier,
        thumb = {
            Box(Modifier.fillMaxSize(.4f))
        },
        track = { hourState ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val hourTickRadius = center.x - 6.dp.toPx()
                        drawEveryInterval(
                            startDegrees = 0f,
                            sweepDegrees = 330f,
                            radius = hourTickRadius,
                            interval = 30f,
                        ) { data ->
                            val isCurrent = degree % 360f == data.intervalDegree
                            rotate(degrees = data.rotationAngle, pivot = data.position) {
                                if (isCurrent) {
                                    drawCircle(
                                        color = accentColor,
                                        center = data.position + Offset(0f, 3.dp.toPx()),
                                        radius = 4.dp.toPx(),
                                        style = Stroke(4f)
                                    )
                                } else {
                                    drawLine(
                                        color = Lime700,
                                        start = data.position,
                                        end = data.position + Offset(0f, 7.dp.toPx()),
                                        strokeWidth = 1.5f.dp.toPx(),
                                        cap = StrokeCap.Round,
                                    )
                                }
                            }
                        }

                        rotate(degrees = hourState.degree + hourState.overshootDegrees) {
                            val handWidth = 12.dp.toPx()
                            val tipLength = center.x * 0.7f
                            val tailLength = 8.dp.toPx()
                            translate(
                                left = center.x - handWidth / 2f,
                                top = center.y - tipLength,
                            ) {
                                drawPath(
                                    path = buildHandPath(
                                        width = handWidth,
                                        height = tipLength + tailLength,
                                        cornerRadius = 12.dp.toPx(),
                                        topCornerRadius = 12.dp.toPx(),
                                    ),
                                    color = Lime500,
                                )
                            }
                        }
                    }
            )
        },
    )
}

@Composable
fun SecondsDial(modifier: Modifier = Modifier) {
    val animatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            animatable.snapTo(0f)
            for (i in 1..60) {
                animatable.animateTo(
                    targetValue = 6f * i,
                    animationSpec = tween(durationMillis = 60, easing = LinearEasing)
                )
            }
            delay(600)
        }
    }

    Dial(
        degree = animatable.value,
        onDegreeChange = { },
        sweepDegrees = 360f,
        startDegrees = 0f,
        interval = 6f,
        modifier = modifier,
        thumb = {
            Box(Modifier.fillMaxSize(.4f))
        },
        track = { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawEveryInterval(
                            startDegrees = 0f,
                            sweepDegrees = 354f,
                            radius = center.x - 4.dp.toPx(),
                            interval = 6f,
                        ) { data ->
                            val isMinuteMark = data.index % 5 == 0
                            rotate(degrees = data.rotationAngle, pivot = data.position) {
                                drawLine(
                                    color = if (isMinuteMark) accentColor else Lime900,
                                    start = data.position,
                                    end = data.position + Offset(
                                        0f,
                                        if (isMinuteMark) 4.dp.toPx() else 2.dp.toPx()
                                    ),
                                    strokeWidth = 1.dp.toPx(),
                                    cap = StrokeCap.Round,
                                )
                            }
                        }

                        rotate(degrees = state.degree + state.overshootDegrees) {
                            val handWidth = 6.dp.toPx()
                            val tipLength = center.x * 0.8f
                            val tailLength = 6.dp.toPx()
                            translate(
                                left = center.x - handWidth / 2f,
                                top = center.y - tipLength,
                            ) {
                                drawPath(
                                    path = buildHandPath(
                                        width = handWidth,
                                        height = tipLength + tailLength,
                                        cornerRadius = 4.dp.toPx(),
                                        topCornerRadius = 8.dp.toPx(),
                                    ),
                                    color = Lime500,
                                )
                            }
                        }
                    }
            )
        },
    )
}

@Composable
fun DayOfWeekDial(modifier: Modifier = Modifier) {
    val interval = 360f / 7f

    var degree by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            degree = 0f
            for (i in 1..7) {
                degree = interval * i
                delay(300)
            }
            delay(600)
        }
    }

    val animatedDegree by animateFloatAsState(
        targetValue = degree,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
        )
    )

    Dial(
        degree = animatedDegree,
        onDegreeChange = { },
        sweepDegrees = 360f,
        startDegrees = 0f,
        interval = interval,
        modifier = modifier,
        thumb = {
            Box(Modifier.fillMaxSize(.4f))
        },
        track = { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawEveryInterval(
                            startDegrees = 0f,
                            sweepDegrees = 360f - interval,
                            radius = center.x - 2.dp.toPx(),
                            interval = interval,
                            currentDegree = state.degree,
                        ) { data ->
                            drawPath(
                                path = createTubePath(
                                    center = center,
                                    radius = center.x - 2.dp.toPx(),
                                    startAngleDegrees = data.intervalDegree - 15f,
                                    sweepAngleDegrees = 30f,
                                    tubeRadius = 2.dp.toPx(),
                                    density = this,
                                ),
                                color = if (degree % 360f == data.intervalDegree) accentColor else Lime500,
                                style = if (degree % 360f == data.intervalDegree) Fill else Stroke(),
                            )
                        }

                        rotate(degrees = state.degree + state.overshootDegrees) {
                            val handWidth = 6.dp.toPx()
                            val tipLength = center.x * 0.7f
                            val tailLength = 5.dp.toPx()
                            translate(
                                left = center.x - handWidth / 2f,
                                top = center.y - tipLength,
                            ) {
                                drawPath(
                                    path = buildHandPath(
                                        width = handWidth,
                                        height = tipLength + tailLength,
                                        cornerRadius = 6.dp.toPx(),
                                        topCornerRadius = 10.dp.toPx(),
                                    ),
                                    color = Lime500,
                                )
                            }
                        }
                    }
            )
        },
    )
}
