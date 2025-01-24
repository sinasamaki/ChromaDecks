package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._001_MeshGradients.components.PointHandle
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.ui.theme.Amber200
import com.sinasamaki.chromadecks.ui.theme.Blue600
import com.sinasamaki.chromadecks.ui.theme.Indigo600
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import org.jetbrains.skiko.currentNanoTime
import kotlin.random.Random

internal data class DefinitionSlideState(
    val showPoints: Boolean,
    val highlightColors: Boolean = false,
    val showGradient: Boolean = false,
    val animate: Boolean = false,
    val animateWildly: Boolean = false,
)

internal class DefinitionSlide : ListSlide<DefinitionSlideState>() {

    override val states: List<DefinitionSlideState>
        get() = listOf(
            DefinitionSlideState(showPoints = false),
            DefinitionSlideState(showPoints = true),
            DefinitionSlideState(
                showPoints = true,
                highlightColors = true,
            ),
            DefinitionSlideState(
                showPoints = true,
                highlightColors = true,
                showGradient = true,
            ),
            DefinitionSlideState(
                showPoints = true,
                highlightColors = true,
                showGradient = true,
                animate = true,
            ),
        )


    @Composable
    override fun content(state: DefinitionSlideState) {

        var tick by remember { mutableStateOf(currentNanoTime()) }
        val spec = remember {
            spring<Offset>(
                stiffness = Spring.StiffnessVeryLow * .5f,
                dampingRatio = Spring.DampingRatioLowBouncy,
                visibilityThreshold = Offset(.001f, .001f)
            )
        }
        LaunchedEffect(Unit) {
            while (true) {
                tick = currentNanoTime()
                delay(400L)
            }
        }

        val middle by animateOffsetAsState(
            targetValue = when {
                state.animate || state.animateWildly -> tick.let {
                    Offset(
                        Random.nextFloat(),
                        Random.nextFloat()
                    )
                }

                else -> Offset(.5f, .5f)
            },
            animationSpec = spec
        )

        val topX by animateOffsetAsState(
            targetValue = when {
                state.animateWildly -> tick.let { Offset(Random.nextFloat(), Random.nextFloat()) }
                state.animate -> tick.let { Offset(Random.nextFloat(), 0f) }
                else -> Offset(.5f, 0f)
            },
            animationSpec = spec
        )

        val leftY by animateOffsetAsState(
            targetValue = when {
                state.animateWildly -> tick.let { Offset(Random.nextFloat(), Random.nextFloat()) }
                state.animate -> tick.let { Offset(0f, Random.nextFloat()) }
                else -> Offset(0f, .5f)
            },
            animationSpec = spec
        )

        val rightY by animateOffsetAsState(
            targetValue = when {
                state.animateWildly -> tick.let { Offset(Random.nextFloat(), Random.nextFloat()) }
                state.animate -> tick.let { Offset(1f, Random.nextFloat()) }
                else -> Offset(1f, .5f)
            },
            animationSpec = spec
        )

        val bottomX by animateOffsetAsState(
            targetValue = when {
                state.animateWildly -> tick.let { Offset(Random.nextFloat(), Random.nextFloat()) }
                state.animate -> tick.let { Offset(Random.nextFloat(), 1f) }
                else -> Offset(.5f, 1f)
            },
            animationSpec = spec
        )

        val points by remember {
            derivedStateOf {
                listOf(
                    listOf(
                        Offset(0f, 0f) to Amber200,
                        topX to Amber200,
                        Offset(1f, 0f) to Amber200,
                    ),
                    listOf(
                        leftY to Rose400,
                        middle to Rose500,
                        rightY to Rose400,
                    ),
                    listOf(
                        Offset(0f, 1f) to Indigo600,
                        bottomX to Blue600,
                        Offset(1f, 1f) to Indigo600,
                    )
                )
            }
        }


        Box(
            modifier = Modifier.fillMaxSize().background(
                color = Slate50,
            ),
            contentAlignment = Alignment.Center,
        ) {

            BoxWithConstraints(
                modifier = Modifier.size(500.dp)
            ) {
                val gradientAlpha by animateFloatAsState(
                    targetValue = if (state.showGradient) 1f else 0f,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(gradientAlpha)
                        .clip(RectangleShape)
                        .meshGradient(
                            points = points,
                            resolutionX = 16,
                            resolutionY = 16,
                        )
                )

                points.flatten().forEach { point ->
                    PointHandle(
                        color = point.second,
                        offset = point.first,
                        showColors = state.highlightColors,
                        showPoints = state.showPoints,
                        contentColor = Zinc900,
                    )
                }
            }
        }
    }
}