package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.sinasamaki.chromadecks._002_PathAnimations.centerPath
import com.sinasamaki.chromadecks._002_PathAnimations.createShape
import com.sinasamaki.chromadecks._002_PathAnimations.createSpiral
import com.sinasamaki.chromadecks._002_PathAnimations.createSpring
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarMoveTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarToCart
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Rose600
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language
import kotlin.math.PI
import kotlin.math.sin

internal data class PolarExamplesState(
    val spiralSides: Int = 4,
    val currentBlock: Int,
) {
    val codeBlocks: List<CodeBlockData>
        get() = listOf(
            CodeBlockData(
                code = getShapeCode(3),
                implementation = {
                    PolarDemoRegularShapes(sides = 3)
                }
            ),
            CodeBlockData(
                code = getShapeCode(4),
                implementation = {
                    PolarDemoRegularShapes(sides = 4)
                }
            ),
            CodeBlockData(
                code = getShapeCode(5),
                implementation = {
                    PolarDemoRegularShapes(sides = 5)
                }
            ),
            CodeBlockData(
                code = getShapeCode(6),
                implementation = {
                    PolarDemoRegularShapes(sides = 6)
                }
            ),
            CodeBlockData(
                code = STAR,
                implementation = {
                    PolarDemoStar(
                        useStroke = false
                    ) {
                        val path = Path()

                        path.relativePolarLineTo(0f, 600f)
                        for (i in 0..2) {
                            path.relativePolarLineTo(36f, 600f)
                        }
                        path.close()
                        path.centerPath(center)
                        path
                    }
                }
            ),
            CodeBlockData(
                code = GEOMETRY_CODE,
                implementation = {
                    GeometryDiagram()
                }
            ),
            //SPIRAL
            CodeBlockData(
                code = SPIRAL,
                implementation = {
                    val value = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        delay(500)
                        value.animateTo(1f, tween(1500))
                    }
                    PolarDemoStar(
                        useStroke = true,
                        progress = value.value
                    ) {
                        val path = createSpiral(
                            center = center,
                            radius = 540f,
                            startRadius = 90f,
                            loops = 5f,
                            sides = spiralSides,
                        )
                        path
                    }
                }
            ),
            //SPRING
            CodeBlockData(
                code = SPRING,
                implementation = {
                    val value = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        delay(500)
                        value.animateTo(1f, tween(1500))
                    }
                    PolarDemoStar(
                        useStroke = true,
                        progress = value.value
                    ) {
                        val path = createSpring(
                            start = Offset.Zero,
                            end = Offset(center.x, 0f),
                            radius = 100f,
                            loops = 3.5f
                        )
                        path.centerPath(center)
                        path
                    }
                }
            ),
            CodeBlockData(
                code = ROSE_CURVE,
                implementation = {
                    val value = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        delay(500)
                        value.animateTo(1f, tween(1500))
                    }
                    PolarDemoStar(
                        useStroke = true,
                        progress = value.value
                    ) {
                        val path = Path().apply {
                            moveTo(center)
                            for (i in 0..300) {
                                val degrees = (i / 300f) * 360f
                                polarLineTo(
                                    degrees = degrees,
                                    distance = (400 * sin((degrees * PI / 180) * 4)).toFloat(),
                                    origin = center
                                )
                            }
                        }
                        path
                    }
                }
            ),
            CodeBlockData(
                code = "FRACTAL",
                implementation = {
                    val value = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        delay(500)
                        value.animateTo(1f, tween(1500))
                    }
                    PolarDemoStar(
                        useStroke = true,
                        progress = 1f,
                        width = 20f
                    ) {
                        val path = Path().apply {
                            moveTo(center.x, size.height)
                            fractal(center = Offset(center.x, size.height))
                        }
                        path
                    }
                }
            ),
        )
}

fun Path.fractal(
    iteration: Int = 0,
    center: Offset = Offset.Zero,
) {
    if (iteration > 3) return
    val length = (320f) / (iteration + 1)
    val split = if (iteration == 0) {
        moveTo(center)
        polarLineTo(degrees = 90f, distance = length, center)
        polarToCart(degrees = 90f, distance = length, center)
    } else {
        center
    }
    val left = polarToCart(degrees = 135f - (iteration * 5), distance = length, origin = split)
    val right = polarToCart(degrees = 45f + (iteration * 5), distance = length, origin = split)
    moveTo(split)
    lineTo(left)
    moveTo(split)
    lineTo(right)
    fractal(iteration + 1, center = left)
    fractal(iteration + 1, center = right)
}

@Language("kotlin")
private val SAMPLE3 = """
val sides = 3
for (i in 1..sides - 1) {
    path.relativePolarLineTo(
        degrees = cornerDegree,
        distance = length
    )
}
path.close()
""".trimIndent()

fun getShapeCode(sides: Int) = """
val sides = ${sides}
for (i in 1..${sides} - 1) {
    path.relativePolarLineTo(
        degrees = cornerDegree,
        distance = length
    )
}
path.close()
""".trimIndent()

@Language("kotlin")
private val STAR = """
path.relativePolarLineTo(0f, 300f)
for (i in 0..2) {
    path.relativePolarLineTo(36f, 300f)
}
path.close()
""".trimIndent()

private const val GEOMETRY_CODE = """val path = Path()
path.polarLineTo(
    degrees = 59f,
    distance = 6f,
)
path.relativePolarLineTo(
    degrees = 97f,
    distance = 13f,
)
path.close()
"""

private const val SPIRAL = """...
spiral.polarLineTo(
    degrees = degree,
    distance = lerp(
        start = startRadius,
        stop = endRadius,
        fraction = f
    ),
    origin = center,
)
...
"""

private const val SPRING = """...
spiral.polarLineTo(
    degrees = degree,
    distance = distance,
    origin = lerp(
        start = startOrigin,
        stop = endOrigin,
        fraction = f
    ),
)
...
"""

private const val ROSE_CURVE = """// Rose Curve
for (i in 0..300) {
    val degrees = (i / 300f) * 360f
    polarLineTo(
        degrees = degrees,
        distance = (400 * sin((degrees * PI / 180) * 4)).toFloat(),
        origin = center
    )
}
"""

internal class PolarExamples : ListSlideAdvanced<PolarExamplesState>() {

    override val initialState: PolarExamplesState
        get() = PolarExamplesState(
            spiralSides = 4,
            currentBlock = 0,
        )

    override val stateMutations: List<PolarExamplesState.() -> PolarExamplesState>
        get() = listOf(
            { copy(currentBlock = 1) },
            { copy(currentBlock = 2) },
            { copy(currentBlock = 3) },
            { copy(currentBlock = 4) },
            { copy(currentBlock = 5) },
            { copy(currentBlock = 6) },
            { copy(spiralSides = 3) },
            { copy(spiralSides = 300) },
            { copy(spiralSides = 6) },
            { copy(currentBlock = 7) },
            { copy(currentBlock = 8) },
        )

    @Composable
    override fun content(state: PolarExamplesState) {

        CodeBlockCarousel(
            codeBlocks = state.codeBlocks,
            currentBlock = state.currentBlock,
        )
    }
}

@Composable
private fun PolarDemoRegularShapes(
    modifier: Modifier = Modifier,
    sides: Int,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .drawPolarGrid(
                color = Slate50,
                alpha = .1f
            )
            .drawBehind {
                val path = createShape(sides, 1000f / ((sides + 1) / 2), center)

                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Sky500,
                            Pink300,
                            Rose600,
                        )
                    ),
                    style = Stroke(
                        width = 50f
                    )
                )
            }
    )
}

@Composable
private fun PolarDemoStar(
    modifier: Modifier = Modifier,
    useStroke: Boolean,
    progress: Float = 1f,
    width: Float = 20f,
    path: DrawScope.() -> Path,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .drawPolarGrid(
                color = Slate50,
                alpha = .1f
            )
            .drawBehind {

                val path = path()
                val measure = PathMeasure().apply {
                    setPath(path, false)
                }
                drawPath(
                    path = path(),
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Sky500,
                            Pink300,
                            Rose600,
                        )
                    ),
                    style = when {
                        useStroke -> Stroke(
                            width = width,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(measure.length * progress, measure.length)
                            )
                        )

                        else -> Fill
                    }
                )
            }
    )
}

@Composable
private fun GeometryDiagram() {

    Box(
        Modifier.fillMaxSize()
            .aspectRatio(1f)
            .drawPolarGrid(
                color = Slate50,
                alpha = .2f
            )
            .drawBehind {
                val path = Path()
                path.polarLineTo(
                    degrees = 59f,
                    distance = 6f,
                )
                path.relativePolarLineTo(
                    degrees = 97f,
                    distance = 13f,
                )
                path.close()
                path.transform(
                    Matrix().apply {
                        scale(40f, 40f)
                    }
                )
                path.addArc(
                    oval = Rect(
                        center = Offset.Zero,
                        radius = 50f,
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = -59f,
                )
                path.addArc(
                    oval = Rect(
                        center = Offset.Zero,
                        radius = 50f,
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = -59f,
                )
                path.centerPath(center)


                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Sky500,
                            Pink300,
                            Rose600,
                        )
                    ),
                    style = Stroke(
                        width = 5f
                    )
                )
            }
    )


}