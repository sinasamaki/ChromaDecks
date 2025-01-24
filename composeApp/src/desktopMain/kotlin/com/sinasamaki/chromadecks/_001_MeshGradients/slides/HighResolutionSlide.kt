package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._001_MeshGradients.PointData
import com.sinasamaki.chromadecks._001_MeshGradients.components.PointHandle
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.extensions.lineTo
import com.sinasamaki.chromadecks.extensions.moveTo
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.components.createHeartPath
import com.sinasamaki.chromadecks.ui.components.renderShading
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Rose700
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random


internal data class HighResolutionSlideState(
    val animateMeshIn: Boolean,
    val showGradient: Boolean,
    val animateGradient: Boolean = false,
    val showLogo: Boolean = false,
)

internal class HighResolutionSlide : ListSlide<HighResolutionSlideState>() {

    override val states: List<HighResolutionSlideState>
        get() = listOf(
            HighResolutionSlideState(
                animateMeshIn = false,
                showGradient = false,
            ),
            HighResolutionSlideState(
                animateMeshIn = true,
                showGradient = false,
            ),
            HighResolutionSlideState(
                animateMeshIn = true,
                showGradient = true,
            ),
            HighResolutionSlideState(
                animateMeshIn = true,
                showGradient = true,
                animateGradient = true,
            ),
            HighResolutionSlideState(
                animateMeshIn = true,
                showGradient = true,
                animateGradient = true,
                showLogo = true,
            ),
        )

    @Composable
    override fun content(state: HighResolutionSlideState) {

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            val pointsAnimation = remember {
                List(size = 4) { y ->
                    List(size = 4) { x ->
                        Animatable(
                            initialValue = Offset(
                                x / 3f,
                                y / 3f,
                            ),
                            typeConverter = Offset.VectorConverter,
                        )
                    }
                }
            }

            LaunchedEffect(state.animateGradient) {
                if (state.animateGradient) {
                    for (y in 0..pointsAnimation.lastIndex) {
                        for (x in 0..pointsAnimation[y].lastIndex) {
                            launch {
                                while (true) {
                                    pointsAnimation[y][x].animateTo(
                                        Offset(
                                            lerp(-.9f, 1.9f, Random.nextFloat()),
                                            lerp(-.9f, 1.9f, Random.nextFloat())
                                        ),
                                        tween(Random.nextInt(10_000, 15_000))
                                    )
                                    delay(Random.nextLong(100, 200))
                                }
                            }
                        }
                    }
                } else {
                    for (y in 0..pointsAnimation.lastIndex) {
                        for (x in 0..pointsAnimation[y].lastIndex) {
                            launch {
                                pointsAnimation[y][x].animateTo(
                                    Offset(
                                        x.toFloat() / pointsAnimation[y].lastIndex,
                                        y.toFloat() / pointsAnimation.lastIndex,
                                    ),
                                    spring(stiffness = Spring.StiffnessLow)
                                )
                            }
                        }
                    }
                }
            }



            Box(
                Modifier.size(500.dp),
                contentAlignment = Alignment.Center
            ) {
                val points by remember {
                    derivedStateOf {
                        listOf(
                            listOf(
                                pointsAnimation[0][0].value to Sky600,
                                pointsAnimation[0][1].value to Sky600,
                                pointsAnimation[0][2].value to Sky600,
                                pointsAnimation[0][3].value to Sky600,
                            ),
                            listOf(
                                pointsAnimation[1][0].value to Yellow300,
                                pointsAnimation[1][1].value to Yellow300,
                                pointsAnimation[1][2].value to Yellow300,
                                pointsAnimation[1][3].value to Yellow300,
                            ),
                            listOf(
                                pointsAnimation[2][0].value to Red400,
                                pointsAnimation[2][1].value to Red400,
                                pointsAnimation[2][2].value to Red400,
                                pointsAnimation[2][3].value to Red400,
                            ),
                            listOf(
                                pointsAnimation[3][0].value to Rose700,
                                pointsAnimation[3][1].value to Rose700,
                                pointsAnimation[3][2].value to Rose700,
                                pointsAnimation[3][3].value to Rose700,
                            ),
                        )
                    }
                }

                val progress by animateFloatAsState(
                    targetValue = if (state.animateMeshIn) 1.01f else .001f,
                    animationSpec = tween(
                        durationMillis = 3_000,
                        easing = LinearEasing,
                    )
                )

                val resolution = remember { 16 }

                Box(
                    Modifier
                        .fillMaxSize()
                        .meshGradient(
                            points = points,
                            resolutionY = resolution,
                            resolutionX = resolution,
                            indicesModifier = {
                                it.take((it.size * progress).roundToInt())
                            }
                        )
                )

                if (!state.showGradient) {
                    val pointData by remember {
                        derivedStateOf {
                            PointData(points, resolution, resolution)
                        }
                    }

                    Box(
                        Modifier.fillMaxSize()
                            .drawBehind {
                                val path = Path()
                                val indicesCount =
                                    (pointData.indices.size * progress).roundToInt()
                                pointData.indices
                                    .forEachIndexed { index, i ->
                                        if (index % 3 == 0)
                                            path.moveTo(pointData.offsets[i] * (size))
                                        else
                                            path.lineTo(pointData.offsets[i] * size)

                                        drawCircle(
                                            color = pointData.colors[i],
                                            center = pointData.offsets[i] * size,
                                            radius = 1.dp.toPx()
                                        )
                                    }

                                drawPath(
                                    path = path,
                                    color = Slate50.copy(alpha = .9f),
                                    style = Stroke(
                                        width = .1f
                                    )
                                )
                            }
                    )
                }

                BoxWithConstraints(
                    Modifier.fillMaxSize()
                ) {
                    points.forEach {
                        it.forEach {
                            PointHandle(
                                color = it.second,
                                offset = it.first,
                                showColors = true,
                                showPoints = !state.showGradient,
                                shadow = Shadow(
                                    color = Zinc950,
                                    blurRadius = 5f,
                                ),
                                contentColor = Slate50
                            )
                        }
                    }

                }

            }

            if (state.showLogo) {
                Box(
                    Modifier.fillMaxSize()
                        .drawBehind {
                            val width = 200.dp.toPx()
                            val height = 200.dp.toPx() * 3 / 2f
                            val heartPath = createHeartPath(
                                size = Size(
                                    width = width,
                                    height = height,
                                ),
                            )

                            heartPath.translate(
                                offset = Offset(
                                    x = size.center.x - (width / 2f),
                                    y = size.center.y - (height / 2f),
                                )
                            )

                            drawPath(
                                path = Path().apply {
                                    addRect(
                                        rect = Rect(
                                            offset = Offset.Zero,
                                            size = size
                                        )
                                    )
                                    op(this, heartPath, PathOperation.Difference)
                                },
                                color = Zinc900
                            )

                            translate(
                                left = size.center.x - (width / 2f),
                                top = size.center.y - (height / 2f),
                            ) {
                                renderShading(width, height)
                            }

                        }
                )

                Text(
                    text = "sinasamaki",
                    modifier = Modifier
                        .padding(bottom = 64.dp)
                        .align(Alignment.BottomCenter),
                    style = MaterialTheme.typography.displaySmall,
                    color = Slate50,
                )
            }
        }

    }
}