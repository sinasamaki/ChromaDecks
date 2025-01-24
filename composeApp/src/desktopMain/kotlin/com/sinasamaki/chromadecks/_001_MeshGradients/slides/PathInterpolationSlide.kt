package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._001_MeshGradients.components.PointHandle
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.extensions.lineTo
import com.sinasamaki.chromadecks.extensions.moveTo
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Amber500
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Rose300
import com.sinasamaki.chromadecks.ui.theme.Sky950
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Violet400

data class PathInterpolationSlideState(
    val code: String,
    val resolution: Int,
    val showLine: Boolean = true,
    val showSampledColor: Boolean = false,
)

private val code = """
    val path = Path()
    path.moveTo(start.x, start.y)
    path.cubicLineTo(
        start.x, start.y,
        end.x - .5f, end.y,
        end.x, end.y,
    )
""".trimIndent()


private fun withResolution(resolution: Int): String = """
    val path = Path()
    path.moveTo(start.x, start.y)
    path.cubicLineTo(
        start.x, start.y,
        end.x - .5f, end.y,
        end.x, end.y,
    )
    val resolution = $resolution
    for (i in 1..<resolution) {
        val sampledOffset = measure.getPosition(
            i / resolution.toFloat() * measure.length
        )
    }
""".trimIndent()

private val withSampledColor = """
    val path = Path()
    path.moveTo(start.x, start.y)
    path.cubicLineTo(
        start.x, start.y,
        end.x - .5f, end.y,
        end.x, end.y,
    )
    val resolution = 16
    for (i in 1..<resolution) {
        val sampledOffset = measure.getPosition(
            i / resolution.toFloat() * measure.length
        )
        
        val sampledColor = lerp(
            startColor,
            endColor,
            i / resolution.toFloat()
        )
    }
""".trimIndent()


class PathInterpolationSlide : ListSlide<PathInterpolationSlideState>() {


    override val states: List<PathInterpolationSlideState>
        get() = listOf(
            PathInterpolationSlideState(
                code = "val path = Path()",
                resolution = 1,
                showLine = false
            ),
            PathInterpolationSlideState(
                code = code,
                resolution = 1,
            ),
            PathInterpolationSlideState(
                code = withResolution(5),
                resolution = 5,
            ),
            PathInterpolationSlideState(
                code = withResolution(16),
                resolution = 16,
            ),
            PathInterpolationSlideState(
                code = withResolution(64),
                resolution = 64,
            ),
            PathInterpolationSlideState(
                code = withResolution(16),
                resolution = 16,
            ),
            PathInterpolationSlideState(
                code = withSampledColor,
                resolution = 16,
                showSampledColor = true,
            ),
        )


    @Composable
    override fun content(state: PathInterpolationSlideState) {

        Row(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            val resolution by animateIntAsState(
                targetValue = state.resolution,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )

            val lineAnimation by animateFloatAsState(
                targetValue = if (state.showLine) 1f else 0f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            Box(Modifier.weight(1f)) {
                CodeBlock(
                    code = state.code
                )
            }

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(48.dp)
            ) {

                val middleColor = remember { Animatable(Amber500) }
                LaunchedEffect(state.showSampledColor) {
                    if (state.showSampledColor) {
                        while (true) {
                            middleColor.animateTo(Violet400, tween(1000))
                            middleColor.animateTo(Amber500, tween(1000))
                        }
                    } else {
                        middleColor.animateTo(Amber500)
                    }
                }
                val points by remember {
                    derivedStateOf {
                        listOf(
                            listOf(
                                Offset(0f, 0f) to Rose300,
                                Offset(.5f, 0f) to Rose300,
                                Offset(1f, 0f) to Rose300,
                            ),

                            listOf(
                                Offset(0f, .7f) to Red500,
                                Offset(.6f, .2f) to middleColor.value,
                                Offset(1f, .3f) to Red500,
                            ),

                            listOf(
                                Offset(0f, 1f) to Sky950,
                                Offset(.5f, 1f) to Sky950,
                                Offset(1f, 1f) to Sky950,
                            ),
                        )
                    }
                }

                val gradientAlpha by animateFloatAsState(
                    targetValue = if (state.showLine) .2f else 1f,
                    animationSpec = tween(500)
                )

                Box(
                    Modifier.fillMaxSize()
                        .alpha(gradientAlpha)
                        .meshGradient(
                            points = points,
                            resolutionX = resolution,
                            resolutionY = 1,
                            showPoints = false,
                        )
                )

                val sampledPointSize by animateDpAsState(
                    targetValue = if (state.showSampledColor) 8.dp else 5.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val topStart = points[0][0].first
                            val topEnd = points[0][1].first
                            val bottomStart = points[2][0].first
                            val bottomEnd = points[2][1].first
                            val topPath = Path().apply {
                                moveTo(topStart)
                                lineTo(topEnd)
                            }
                            val bottomPath = Path().apply {
                                moveTo(bottomStart)
                                lineTo(bottomEnd)
                            }

                            val start = points[1][0].first
                            val end = points[1][1].first

                            val startColor = points[1][0].second
                            val endColor = points[1][1].second

                            val path = Path()

                            path.moveTo(start.x, start.y)
                            val delta = (end.x - start.x) * .8f
                            path.cubicTo(
                                start.x, start.y,
                                end.x - delta, end.y,
                                end.x, end.y,
                            )

                            val measure = PathMeasure()
                            measure.setPath(path, false)
                            scale(
                                scaleX = size.width,
                                scaleY = size.height,
                                pivot = Offset.Zero,
                            ) {
                                drawPath(
                                    path = path,
                                    color = Slate50,
                                    style = Stroke(
                                        width = .003f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            intervals = floatArrayOf(
                                                measure.length * 1.1f * lineAnimation,
                                                measure.length,
                                            )
                                        )
                                    )
                                )
                            }

                            val edgePathMeasure = PathMeasure()
                            if (resolution > 1) {
                                for (i in 1..<resolution) {
                                    val sampledOffset = measure.getPosition(
                                        distance = i / resolution.toFloat() * measure.length
                                    )

                                    listOf(topPath, bottomPath).forEach { edgePath ->
                                        edgePathMeasure.setPath(edgePath, false)
                                        val edgePoint = edgePathMeasure.getPosition(
                                            distance = i / resolution.toFloat() * edgePathMeasure.length
                                        ) * size
                                        drawLine(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Transparent,
                                                    Slate50.copy(alpha = .4f),
                                                    Transparent,
                                                )
                                            ),
                                            start = sampledOffset * size,
                                            end = edgePoint,
                                            strokeWidth = 1f
                                        )

                                        drawCircle(
                                            color = Slate50.copy(alpha = .4f),
                                            center = edgePoint,
                                            radius = 1.dp.toPx(),
                                            style = Stroke(width = 2f)
                                        )
                                    }

                                    val sampledColor = lerp(
                                        startColor,
                                        endColor,
                                        i / resolution.toFloat()
                                    )



                                    drawCircle(
                                        color = Slate50,
                                        center = sampledOffset * size,
                                        radius = sampledPointSize.toPx(),
                                        style = Stroke(width = 2f)
                                    )

                                    if (state.showSampledColor) {
                                        drawCircle(
                                            color = sampledColor,
                                            center = sampledOffset * size,
                                            radius = sampledPointSize.toPx(),
                                        )
                                    }
                                }
                            }
                        }
                )

                points.flatten().forEach { (offset, color) ->
                    PointHandle(
                        color = color,
                        offset = offset,
                        showColors = true,
                        showPoints = true,
                        contentColor = Slate50,
                    )
                }
            }
        }
    }
}