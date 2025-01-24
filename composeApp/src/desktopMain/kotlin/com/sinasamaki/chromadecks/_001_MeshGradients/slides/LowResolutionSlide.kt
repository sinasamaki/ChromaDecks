package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._001_MeshGradients.PointData
import com.sinasamaki.chromadecks._001_MeshGradients.components.PointHandle
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.extensions.lineTo
import com.sinasamaki.chromadecks.extensions.moveTo
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.theme.Blue400
import com.sinasamaki.chromadecks.ui.theme.Indigo200
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Rose700
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlin.math.roundToInt


internal data class LowResolutionSlideState(
    val showPoints: Boolean = true,
    val showColors: Boolean = true,
    val animateMeshIn: Boolean,
    val showGradient: Boolean = false,
    val animatePoints: Boolean = false,
)

internal class LowResolutionSlide : ListSlide<LowResolutionSlideState>() {

    override val states: List<LowResolutionSlideState>
        get() = listOf(
            LowResolutionSlideState(
                showPoints = false,
                showColors = false,
                animateMeshIn = false,
            ),
            LowResolutionSlideState(
                showColors = false,
                animateMeshIn = false,
            ),
            LowResolutionSlideState(
                animateMeshIn = false,
            ),
            LowResolutionSlideState(
                animateMeshIn = true,
            ),
            LowResolutionSlideState(
                animateMeshIn = true,
                showGradient = true,
            ),
            LowResolutionSlideState(
                animateMeshIn = true,
                showGradient = true,
                animatePoints = true,
            ),
        )

    @Composable
    override fun content(state: LowResolutionSlideState) {

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            val pointTranslate = remember { Animatable(0f) }
            LaunchedEffect(state.animatePoints) {
                if (state.animatePoints) {
                    while (true) {
                        pointTranslate.animateTo(.3f, tween(1000))
                        pointTranslate.animateTo(-.3f, tween(1000))
                    }
                } else {
                    pointTranslate.animateTo(0f, spring())
                }
            }


            Box(Modifier.size(500.dp)) {
                val points by remember {
                    derivedStateOf {
                        listOf(
                            listOf(
                                Offset(0f, 0f) to Rose700,
                                Offset(.33f, 0f) to Rose700,
                                Offset(.66f, 0f) to Rose700,
                                Offset(1f, 0f) to Rose700,
                            ),

                            listOf(
                                Offset(0f, .33f) to Purple500,
                                Offset(.33f, .33f - pointTranslate.value) to Purple500,
                                Offset(.66f, .33f + pointTranslate.value) to Purple500,
                                Offset(1f, .33f) to Purple500,
                            ),

                            listOf(
                                Offset(0f, .66f) to Blue400,
                                Offset(.33f, .66f - pointTranslate.value) to Blue400,
                                Offset(.66f, .66f + pointTranslate.value) to Blue400,
                                Offset(1f, .66f) to Blue400,
                            ),

                            listOf(
                                Offset(0f, 1f) to Indigo200,
                                Offset(.33f, 1f) to Indigo200,
                                Offset(.66f, 1f) to Indigo200,
                                Offset(1f, 1f) to Indigo200,
                            ),
                        )
                    }
                }

                val progress by animateFloatAsState(
                    targetValue = if (state.animateMeshIn) 1.01f else .01f,
                    animationSpec = tween(
                        durationMillis = 2_000,
                        easing = LinearEasing,
                    )
                )

                val gradientAlpha by animateFloatAsState(
                    targetValue = if (state.showGradient) 1f else .2f,
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .alpha(alpha = gradientAlpha)
                        .clip(RectangleShape)
                        .meshGradient(
                            points = points,
                            indicesModifier = {
                                it.take((it.size * progress).roundToInt())
                            },
                        )
                )

                val pointData by remember {
                    derivedStateOf {
                        PointData(points, 1, 1)
                    }
                }

                Box(
                    Modifier.fillMaxSize()
                        .alpha(1f - gradientAlpha)
                        .drawBehind {
                            val path = Path()
                            pointData.indices
                                .take((pointData.indices.size * progress).roundToInt())
                                .forEachIndexed { index, i ->
                                    if (index % 3 == 0)
                                        path.moveTo(pointData.offsets[i] * (size))
                                    else
                                        path.lineTo(pointData.offsets[i] * size)

                                    drawCircle(
                                        color = Slate50,
                                        center = pointData.offsets[i] * size,
                                        radius = 1f
                                    )

                                }
                            drawPath(
                                path = path,
                                color = Slate50.copy(alpha = .9f),
                                style = Stroke(
                                    width = 1.8f
                                )
                            )
                        })

                BoxWithConstraints(
                    Modifier.fillMaxSize()
                ) {
                    points.forEach {
                        it.forEach {
                            PointHandle(
                                color = it.second,
                                offset = it.first,
                                showColors = state.showColors,
                                showPoints = state.showPoints && !state.showGradient,
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
        }

    }
}