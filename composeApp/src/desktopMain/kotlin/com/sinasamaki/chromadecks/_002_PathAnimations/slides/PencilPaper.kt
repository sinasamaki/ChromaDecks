package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Blue600
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Slate100
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

internal data class PencilPaperState(
    val tiltPaper: Boolean = false,
    val showPencil: Boolean = false,
    val onPaper: Boolean = true,
    val pathCode: String = "",
    val drawCode: String = "",
    val showDrawCode: Boolean = false,
    val useGradient: Boolean = false,
    val useStroke: Boolean = false,
    val width: Int = 10,
    val showCapAndJoint: Boolean = false,
    val teasePathEffect: Boolean = false,
)

private const val INIT = """
val path = Path()
"""

private const val MOVE_TO = """
val path = Path()
path.moveTo(30f, 30f)
"""

private const val LINE_FIRST = """
val path = Path()
path.moveTo(30f, 30f)
path.lineTo(30f, 70f)
"""

private const val LINE_SECOND = """
val path = Path()
path.moveTo(30f, 30f)
path.lineTo(30f, 70f)
path.lineTo(70f, 70f)
"""

private const val RELATIVE_FIRST = """
val path = Path()
path.moveTo(30f, 30f)
path.lineTo(30f, 70f)
path.lineTo(70f, 70f)
path.relativeLineTo(-20f, -20f)
"""

private const val RELATIVE_SECOND = """
val path = Path()
path.moveTo(30f, 30f)
path.lineTo(30f, 70f)
path.lineTo(70f, 70f)
path.relativeLineTo(-20f, -20f)
path.relativeLineTo(20f, -20f)
"""

private const val CLOSE_PATH = """
val path = Path()
path.moveTo(30f, 30f)
path.lineTo(30f, 70f)
path.lineTo(70f, 70f)
path.relativeLineTo(-20f, -20f)
path.relativeLineTo(20f, -20f)
path.close()
"""

private fun getDrawCode(
    showDrawCode: Boolean = true,
    useGradient: Boolean = true,
    useStroke: Boolean = true,
    width: Int = 10,
    showCapAndJoint: Boolean = true,
    teasePathEffect: Boolean = true,
): String {
    if (!showDrawCode) return ""
    return """
drawPath(
    path = path,
    ${
        if (!useGradient) "color = Blue600," else
            """brush = Brush.horizontalGradient(
        colors = listOf(Purple400, Indigo500)
    ),"""
    }
    ${
        if (!useStroke) "style = Fill" else
            """style = Stroke(
        width = ${width}f,${
                if (showCapAndJoint) {
                    """ 
        cap = StrokeCap.Round,
        joint = StrokeJoint.Round,"""
                } else ""
            }${
                if (teasePathEffect) {
                    """
        pathEffect = PathEffect
          .dashedPathEffect(
            intervals = floatArrayOf(100f, 100f)
        )"""
                } else ""
            }
    )
        """.trimIndent()
    }
)
"""
}


internal class PencilPaper : ListSlideAdvanced<PencilPaperState>() {

    override val initialState: PencilPaperState
        get() = PencilPaperState(
            tiltPaper = false,
            showPencil = false,
            onPaper = false,
            pathCode = INIT,
        )

    override val stateMutations: List<PencilPaperState.() -> PencilPaperState>
        get() = listOf(
            { copy(tiltPaper = true) },
            { copy(showPencil = true) },
            {
                copy(pathCode = MOVE_TO)
            },
            { copy(onPaper = true) },
            {
                copy(pathCode = LINE_FIRST)
            },
            {
                copy(pathCode = LINE_SECOND)
            },
            {
                copy(pathCode = RELATIVE_FIRST)
            },
            {
                copy(pathCode = RELATIVE_SECOND)
            },
            {
                copy(pathCode = CLOSE_PATH)
            },
            { copy(showPencil = false, tiltPaper = false) },
            { copy(showDrawCode = true) },
            { copy(useGradient = true) },
            { copy(useStroke = true) },
            { copy(width = 40) },
            { copy(showCapAndJoint = true) },
            { copy(teasePathEffect = true) },
        )

    @Composable
    override fun content(state: PencilPaperState) {
        var origin by remember { mutableStateOf(Offset.Zero) }
        Box(
            Modifier
                .fillMaxSize()
                .background(Slate100)
                .onGloballyPositioned {
                    origin = it.positionInWindow()
                }
        ) {

            val range = remember { 100 }

            var coordinates by remember { mutableStateOf(Offset.Zero) }
            var coordinatesOnScreen by remember { mutableStateOf(Offset(0f, 0f)) }

            val paperRotation by animateFloatAsState(
                targetValue = if (state.tiltPaper) 60f else 0f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy
                )
            )

            val (kotlinPath, distances) = remember {
                val measure = PathMeasure()
                val distances = mutableListOf<Float>()
                distances.add(0f)
                Path().apply {
                    moveTo(30f, 30f)

                    lineTo(30f, 70f)
                    measure.setPath(this, false)
                    distances.add(measure.length)

                    lineTo(70f, 70f)
                    measure.setPath(this, false)
                    distances.add(measure.length)

                    relativeLineTo(-20f, -20f)
                    measure.setPath(this, false)
                    distances.add(measure.length)

                    relativeLineTo(20f, -20f)
                    measure.setPath(this, false)
                    distances.add(measure.length)

                    close()
                    measure.setPath(this, true)
                    distances.add(measure.length)
                } to distances
            }

            val currentDistance by animateFloatAsState(
                targetValue = when (state.pathCode) {
                    MOVE_TO -> .001f
                    LINE_FIRST -> distances[1]
                    LINE_SECOND -> distances[2]
                    RELATIVE_FIRST -> distances[3]
                    RELATIVE_SECOND -> distances[4]
                    CLOSE_PATH -> distances[5]
                    else -> 0f
                },
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val animatedStrokeWidth by animateIntAsState(
                    targetValue = state.width,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                    )
                )
                CodeBlock(
                    modifier = Modifier.width(600.dp),
                    code = "${state.pathCode}${
                        getDrawCode(
                            showDrawCode = state.showDrawCode,
                            useGradient = state.useGradient,
                            useStroke = state.useStroke,
                            width = animatedStrokeWidth,
                            showCapAndJoint = state.showCapAndJoint,
                            teasePathEffect = state.teasePathEffect,
                        )
                    }",
                    darkMode = false,
                    fadeAnimations = false
//                    fadeAnimations = animatedStrokeWidth == state.width
                )
                Spacer(Modifier.width(56.dp))
                BoxWithConstraints(
                    Modifier
//                    .align(Alignment.Center)
                        .graphicsLayer {
                            rotationX = paperRotation
                            cameraDistance = 20f
                        }
                        .clip(RoundedCornerShape(4.dp))
                        .background(Slate50)
                        .drawCartesianGrid(range = 10)
                        .size(500.dp)
                        .border(
                            width = 1.dp,
                            color = Slate950,
                            shape = RoundedCornerShape(4.dp)
                        )

                ) {
                    val cellSize = maxWidth.toPx / range

                    Box(
                        Modifier.fillMaxSize()
                            .drawBehind {

                                val unScaledMeasure = PathMeasure()
                                unScaledMeasure.setPath(kotlinPath, true)

                                if (currentDistance > 0f) {
                                    coordinates = unScaledMeasure.getPosition(
                                        currentDistance
                                    )
                                } else {
                                    coordinates = Offset.Zero
                                }

                                val scaledPath = scalePath(kotlinPath)

                                val measure = PathMeasure()
                                measure.setPath(scaledPath, true)

                                if (!state.showDrawCode)
                                    drawPath(
                                        path = scaledPath,
                                        color = Neutral950,
                                        style = Stroke(
                                            width = 8f,
                                            pathEffect = PathEffect.dashPathEffect(
                                                intervals = floatArrayOf(
                                                    measure.length * (currentDistance / distances.last()),
                                                    measure.length
                                                )
                                            )
                                        )
                                    )


                                if (state.showDrawCode) {
                                    val brush = Brush.verticalGradient(
                                        colors = listOf(Sky600, Violet400),
                                        startY = scaledPath.getBounds().top,
                                        endY = scaledPath.getBounds().bottom,
                                    )
                                    if (!state.useStroke) {
                                        if (!state.useGradient) {
                                            drawPath(
                                                path = scaledPath,
                                                color = Blue600,
                                                style = Fill
                                            )
                                        } else {
                                            drawPath(
                                                path = scaledPath,
                                                brush = brush,
                                                style = Fill
                                            )
                                        }
                                    } else {
                                        drawPath(
                                            path = scaledPath,
                                            brush = brush,
                                            style = Stroke(
                                                width = animatedStrokeWidth.toFloat(),
                                                cap = if (state.showCapAndJoint) StrokeCap.Round else StrokeCap.Butt,
                                                join = if (state.showCapAndJoint) StrokeJoin.Round else StrokeJoin.Miter,
                                                pathEffect = if (state.teasePathEffect)
                                                    PathEffect.dashPathEffect(
                                                        intervals = floatArrayOf(
                                                            50f, 50f
                                                        )
                                                    )
                                                else null
                                            )
                                        )
                                    }
                                }
                            }
                    )

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (coordinates.x * cellSize).roundToInt(),
                                    y = (coordinates.y * cellSize).roundToInt(),
                                )
                            }
                            .onGloballyPositioned {
                                coordinatesOnScreen = Offset(
                                    it.positionInWindow().x,
                                    it.positionInWindow().y,
                                )
                            }
                    )
                }
            }

            val animatedCoordinates by animateOffsetAsState(
                coordinatesOnScreen,
                if (currentDistance < .1f) spring(stiffness = Spring.StiffnessVeryLow) else snap()
            )

            val animatedRotation by animateFloatAsState(
                targetValue = lerp(
                    start = 50f,
                    stop = -30f,
                    (coordinates.x / range.toFloat()),
                ),
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )


            Pencil(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        (animatedCoordinates - origin).toIntOffset()
                    },
                rotation = {
                    animatedRotation
                },
                showPencil = state.showPencil,
                liftPencil = !state.onPaper,

                )
        }

    }
}

@Composable
fun Pencil(
    modifier: Modifier = Modifier,
    rotation: () -> Float = { 0f },
    showPencil: Boolean = true,
    liftPencil: Boolean = true,
) {
//    val rotation by animateFloatAsState(
//        targetValue = rotation(), snap()
//    )

    val alpha by animateFloatAsState(
        targetValue = if (showPencil) 1f else 0f
    )

    val infinite = rememberInfiniteTransition()
    val hoverAnimation by infinite.animateFloat(
        initialValue = 20f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val liftOffset by animateFloatAsState(
        targetValue = when {
            showPencil -> if (liftPencil) -hoverAnimation else 0f
            else -> -300f
        }
    )

    Column(
        modifier
            .offset {
                IntOffset(
                    x = (-10.dp).toPx().toInt(),
                    y = (-200.dp).toPx().toInt(),
                )
            }
            .graphicsLayer {
//                this.alpha = alpha
                rotationZ = rotation()
                transformOrigin = TransformOrigin(.5f, 1f)
                clip = false
                compositingStrategy = CompositingStrategy.Auto
            }
            .drawBehind {
                val shadowSize = Size(
                    (liftOffset * .5f).absoluteValue.coerceAtMost(30f),
                    (liftOffset * .5f).absoluteValue.coerceAtMost(30f),
                )

                drawOval(
                    color = Slate950.copy(alpha * .4f),
                    topLeft = Offset(
                        size.width / 2, size.height
                    ) - Offset(
                        shadowSize.width / 2,
                        shadowSize.height / 2
                    ),
                    size = shadowSize
                )
            }
            .offset {
                IntOffset(0, liftOffset.toInt())
            }
            .alpha(alpha)
            .height(200.dp)
            .width(20.dp)
    ) {
        Box(
            Modifier
                .height(20.dp)
                .width(20.dp)
                .background(
                    color = Pink400,
                    shape = RoundedCornerShape(
                        topStart = 5.dp,
                        topEnd = 5.dp,
                    )
                )
        )

        Box(
            Modifier
                .weight(1f)
                .width(20.dp)
                .background(
                    color = Yellow500,
                )
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .background(
                        color = Slate950.copy(alpha = .2f)
                    )
            )
        }

        Box(
            Modifier
                .height(10.dp)
                .width(20.dp)
                .background(
                    color = Slate950,
                    shape = CutCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                )
        )
    }

}

@Composable
fun Modifier.drawCartesianGrid(
    color: Color = Slate950,
    range: Int = 10,
    alpha: Float = .6f,
): Modifier {
    return this.drawBehind {
        for (x in 0..range) {
            val xPoint = (x / range.toFloat()) * size.width
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(xPoint, 0f),
                end = Offset(xPoint, size.height),
            )
        }

        for (y in 0..range) {
            val yPoint = (y / range.toFloat()) * size.height
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(0f, yPoint),
                end = Offset(size.width, yPoint),
            )
        }
    }
}

@Composable
fun Modifier.drawPolarGrid(
    color: Color = Slate950,
    range: Int = 8,
    alpha: Float = .6f,
): Modifier {
    return this.drawWithContent {
        drawContent()
        drawRect(
            color = color,
            alpha = alpha,
            style = Stroke(1f),
        )
        clipRect {
            for (theta in 0 until range) {
                rotate(
                    ((theta / range.toFloat()) * 180f) + 45f
                ) {
                    drawLine(
                        color = color.copy(alpha = alpha),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, 0f),
                    )
                }

            }

            for (distIncrement in 0..range) {
                val radius = distIncrement / range.toFloat() * size.height
                drawCircle(
                    color = color.copy(alpha = alpha),
                    style = Stroke(1f),
                    radius = radius
                )
            }
        }
    }
}