package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addSvg
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.getDegrees
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.nextFloat
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Emerald400
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Slate100
import kotlin.math.absoluteValue
import kotlin.random.Random

private const val MEASURE_INIT = """
val pathMeasure = PathMeasure()
"""

private const val MEASURE_SET = """
val pathMeasure = PathMeasure()
pathMeasure.setPath(path, false)
"""

private const val ANIMATE_DASH = """
pathEffect = PathEffect.dashPathEffect(
    intervals = floatArrayOf(
        pathMeasure.length * value,
        pathMeasure.length
    )
)
"""


private const val ANIMATE_OBJECT = """
drawCircle(
    color = Sky600,
    center = pathMeasure.getPosition(
        pathMeasure.length * value
    ),
    radius = 200f
)
"""

private fun stampCloneCode(
    lerpColor: Boolean,
    lerpSize: Boolean,
) = """
    for (i in 0 .. iterations) {
        val x = i / iterations.toFloat()
        val position = pathMeasure.getPosition(
            pathMeasure.length * x
        )
        drawCircle(
            color = ${if (lerpColor) "lerp(Sky600, Emerald400)" else "Sky600"},
            center = position,
            radius = ${if (lerpSize) "lerp(10f, 50f)" else "50f"}
        )
    }
""".trimIndent()

private const val UN_ROTATED_OBJECT = """
translate(
    position.x,
    position.y
) {
    drawPath(
        path = arrow,
        color = Sky600,
    )
}
"""

private const val TANGENT = """
val tangent = pathMeasure.getTangent(
    pathMeasure.length * value
)
"""

private const val ROTATED_OBJECT = """
rotate(
    degrees = getDegrees(tangent),
    pivot = position,
) {
    translate(position.x, position.y) {
        drawPath(
            path = arrow,
            color = Sky600,
        )
    }
}
"""

//private val path = Path().apply {
//    lineTo(100f, 100f)
//}

private val path = Path().apply {
    moveTo(50f, 50f)
//    createPath(
//        points = buildList {
//            add(Offset(Random.nextFloat() * 100, Random.nextFloat() * 100))
//            add(Offset(Random.nextFloat() * 100, Random.nextFloat() * 100))
//            add(Offset(Random.nextFloat() * 100, Random.nextFloat() * 100))
//            add(Offset(Random.nextFloat() * 100, Random.nextFloat() * 100))
//            add(Offset(Random.nextFloat() * 100, Random.nextFloat() * 100))
//        }
//    ).let { (path, _ )-> addPath(path) }

    addSvg(
        pathData = """
m 3.4838576,79.923792 c 0,0 37.8638934,12.555415 33.4664384,-34.350722 C 35.105899,25.899521 53.369231,9.0004471 78.28433,14.755162 c 32.81616,7.579645 20.41606,63.795956 -10e-7,72.751144 C 52.65657,98.747524 37.226883,83.706169 19.143843,53.336425 0.17825075,21.484471 22.333506,4.1717029 22.333506,4.1717029
        """.trimIndent()
    )

}
private val brush = Brush.verticalGradient(
    colors = listOf(
        Sky600, Emerald400,
    )
)

internal data class PathMeasuringState(
    val currentBlock: Int,
    val pathMeasureCode: String,
    val animateDashEffect: Boolean = false,
    val animateStampSize: Boolean = false,
    val animateStampColor: Boolean = false,
    val showTangent: Boolean = false,
    val rotateArrow: Boolean = false,
) {

    val codeBlocks: List<CodeBlockData>
        get() = listOf(
            CodeBlockData(
                code = pathMeasureCode,
                implementation = {
                    val infinite = rememberInfiniteTransition()
                    val value by infinite.animateFloat(
                        initialValue = 0f,
                        targetValue = if (animateDashEffect) 1f else 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 2000
                            )
                        )
                    )
                    Box(
                        Modifier.fillMaxSize()
                            .aspectRatio(1f)
                            .drawCartesianGrid(
                                color = Slate100,
                                alpha = .1f
                            )
                            .drawBehind {
                                val scaledPath = scalePath(path)
                                val pathMeasure = PathMeasure()
                                pathMeasure.setPath(scaledPath, false)
                                drawPath(
                                    path = scaledPath,
                                    brush = brush,
                                    style = Stroke(
                                        width = 100f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            intervals = floatArrayOf(
                                                pathMeasure.length * value, pathMeasure.length
                                            )
                                        )
                                    )
                                )
                            }
                    )
                }
            ),
            CodeBlockData(
                code = ANIMATE_OBJECT,
                implementation = {
                    val infinite = rememberInfiniteTransition()
                    val value by infinite.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 2000
                            )
                        )
                    )
                    Box(
                        Modifier.fillMaxSize()
                            .aspectRatio(1f)
                            .drawCartesianGrid(
                                color = Slate100,
                                alpha = .1f
                            )
                            .drawBehind {
                                val scaledPath = scalePath(path)
                                val pathMeasure = PathMeasure()
                                pathMeasure.setPath(scaledPath, false)
                                drawPath(
                                    path = scaledPath,
                                    color = Blue500,
                                    style = Stroke(
                                        width = 1f,
                                    )
                                )
                                drawCircle(
                                    color = Sky600,
                                    center = pathMeasure.getPosition(
                                        pathMeasure.length * value
                                    ),
                                    radius = 50f
                                )
                            }
                    )
                }
            ),
            CodeBlockData(
                code = stampCloneCode(
                    lerpColor = animateStampColor,
                    lerpSize = animateStampSize,
                ),
                implementation = {
                    val infinite = rememberInfiniteTransition()
                    val value by infinite.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 3000,
                            ),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Box(
                        Modifier.fillMaxSize()
                            .aspectRatio(1f)
                            .drawCartesianGrid(
                                color = Slate100,
                                alpha = .1f
                            )
                            .drawBehind {
                                val scaledPath = scalePath(path)
                                val pathMeasure = PathMeasure()
                                pathMeasure.setPath(scaledPath, false)
                                drawPath(
                                    path = scaledPath,
                                    color = Blue500,
                                    style = Stroke(
                                        width = 1f,
                                    )
                                )
                                val iterations = lerp(1, 70, value)
                                for (i in 0..iterations) {
                                    val x = i / iterations.toFloat()
                                    val position = pathMeasure.getPosition(
                                        pathMeasure.length * x
                                    )
                                    drawCircle(
                                        color = if (animateStampColor) androidx.compose.ui.graphics.lerp(
                                            Sky600,
                                            Emerald400,
                                            x
                                        ) else Sky600,
                                        center = position,
                                        radius = if (animateStampSize)
                                            lerp(10f, 50f, x)
                                        else
                                            50f
                                    )
                                }
                            }
                    )
                }
            ),
            CodeBlockData(
                code = when {
                    showTangent -> TANGENT
                    rotateArrow -> ROTATED_OBJECT
                    else -> UN_ROTATED_OBJECT
                },
                implementation = {
                    val infinite = rememberInfiniteTransition()
                    val value by infinite.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 4000,
                            )
                        )
                    )
                    Box(
                        Modifier.fillMaxSize()
                            .aspectRatio(1f)
                            .drawCartesianGrid(
                                color = Slate100,
                                alpha = .1f
                            )
                            .drawBehind {
                                val scaledPath = scalePath(path)
                                val pathMeasure = PathMeasure()
                                pathMeasure.setPath(scaledPath, false)
                                drawPath(
                                    path = scaledPath,
                                    color = Blue500,
                                    style = Stroke(
                                        width = 1f,
                                    )
                                )

                                val arrow = Path().apply {

                                    moveTo(0f, -30f)
                                    lineTo(0f, 30f)
                                    lineTo(30f, 0f)

                                    moveTo(0f, 0f)
                                    relativeLineTo(0f, -5f)
                                    relativeLineTo(-50f, 0f)
                                    relativeLineTo(0f, 10f)
                                    relativeLineTo(50f, 0f)

                                    transform(
                                        matrix = Matrix().apply {
                                            scale(x = 2f, y = 2f)
                                        }
                                    )
                                }

                                val position = pathMeasure.getPosition(
                                    pathMeasure.length * value
                                )
                                val tangent = pathMeasure.getTangent(
                                    pathMeasure.length * value
                                )

                                if (showTangent) {
                                    rotate(
                                        degrees = getDegrees(tangent),
                                        pivot = position,
                                    ) {
                                        translate(
                                            position.x,
                                            position.y
                                        ) {
                                            drawPath(
                                                path = Path().apply {
                                                    lineTo(0f, -100f)
                                                },
                                                color = Green500,
                                                style = Stroke(15f, cap = StrokeCap.Round)
                                            )
                                            drawPath(
                                                path = Path().apply {
                                                    lineTo(100f, 0f)
                                                },
                                                color = Red500,
                                                style = Stroke(15f, cap = StrokeCap.Round)
                                            )
                                        }
                                    }
                                } else {
                                    rotate(
                                        degrees = if (rotateArrow) getDegrees(tangent) else 0f,
                                        pivot = position,
                                    ) {
                                        translate(
                                            position.x,
                                            position.y
                                        ) {
                                            drawPath(
                                                path = arrow,
                                                color = Sky600,
                                            )
                                        }
                                    }
                                }
                            }
                    )
                }
            ),
        )

}

internal class PathMeasuring : ListSlideAdvanced<PathMeasuringState>() {

    override val initialState: PathMeasuringState
        get() = PathMeasuringState(
            currentBlock = 0,
            pathMeasureCode = MEASURE_INIT,
        )

    override val stateMutations: List<PathMeasuringState.() -> PathMeasuringState>
        get() = listOf(
            { copy(pathMeasureCode = MEASURE_SET, currentBlock = 0) },
            { copy(pathMeasureCode = ANIMATE_DASH, animateDashEffect = true) },
            { copy(currentBlock = 1) },
            { copy(currentBlock = 2) },
            { copy(animateStampSize = true) },
            { copy(animateStampColor = true) },
            { copy(currentBlock = 3) },
            { copy(showTangent = true) },
            {
                copy(
                    showTangent = false,
                    rotateArrow = true,
                )
            }
        )

    @Composable
    override fun content(state: PathMeasuringState) {
        CodeBlockCarousel(
            codeBlocks = state.codeBlocks,
            currentBlock = state.currentBlock,
            codeChangeAnimations = true,
        )
    }
}