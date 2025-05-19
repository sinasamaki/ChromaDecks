package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.createRoseCurve
import com.sinasamaki.chromadecks._002_PathAnimations.createShape
import com.sinasamaki.chromadecks._002_PathAnimations.createSpiral
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Teal400
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlin.math.absoluteValue
import kotlin.math.sin

internal data class ChapterOverviewState(
    val showEffect: Boolean = false,
    val showMeasure: Boolean = false,
)

internal class ChapterOverview : ListSlideAdvanced<ChapterOverviewState>() {

    override val initialState: ChapterOverviewState
        get() = ChapterOverviewState()

    override val stateMutations: List<ChapterOverviewState.() -> ChapterOverviewState>
        get() = listOf(
            { copy(showEffect = true) },
            { copy(showMeasure = true) },
        )

    @Composable
    override fun content(state: ChapterOverviewState) {
//        PromoTest().content()
//        return
        Row(
            modifier = Modifier
                .background(Slate50)
                .padding(
                    horizontal = 32.dp,
                    vertical = 128.dp,
                )
        ) {
            val infinite = rememberInfiniteTransition()

            val drawingValue by infinite.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 10_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                )
            )

            Section(
                title = "Path Drawing",
                visible = true,
                onDraw = {
                    val canvasSize = size
                    val (path, control) = createPath(
                        points = listOf(
                            Offset(x = 0.1f, y = 0.1f),
                            Offset(x = 0.9f, y = 0.25f),
                            Offset(x = 0.1f, y = 0.5f),
                            Offset(x = 0.99f, y = 0.5f),
                            Offset(x = 0.1f, y = 0.75f),
                        ).map {
                            it * canvasSize
                        }
//                        points = buildList {
////                            for (i in 0..10) {
////                                add(
////                                    Offset(
////                                        x = Random.nextFloat(-1f, 1f) * canvasSize.width,
////                                        y = Random.nextFloat(-1f, 1f) * canvasSize.height,
////                                    )
////                                )
////                            }
//                        }
                    )

                    val square = createShape(
                        sides = 6,
                        length = 200f,
                        center = Offset(
                            x = size.width * .2f,
                            y = size.height * .3f,
                        )
                    )

                    val roseCurve = createRoseCurve(
                        center = Offset(
                            size.width * .7f,
                            size.height * .8f,
                        ),
                        radius = 300f,
                        petals = 4
                    )

                    val spiral = createSpiral(
                        center = Offset(
                            x = size.width * .8f,
                            y = size.height * .1f,
                        ),
                        startRadius = 100f,
                        radius = 200f,
                        sides = 300,
                    )
                    onDrawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Sky400,
                                    Purple400,
                                    Pink300,
                                )
                            )
                        )

//                        drawPath(
//                            path = path,
//                            color = Black,
//                            style = Stroke(
//                                width = 4f
//                            )
//                        )

                        rotate(
                            degrees = -120f * drawingValue,
                            pivot = square.getBounds().center
                        ) {
                            drawPath(
                                path = square,
                                color = Black,
                                style = Stroke(
                                    width = 4f
                                )
                            )
                        }

                        translate(
                            top = FastOutSlowInEasing.transform((drawingValue - .5f).absoluteValue) * -100f
                        ) {
                            rotate(
                                degrees = 360f * drawingValue,
                                pivot = roseCurve.getBounds().center
                            ) {
                                drawPath(
                                    path = roseCurve,
                                    color = Black,
                                    style = Stroke(
                                        width = 4f
                                    )
                                )
                            }
                        }

                        rotate(
                            degrees = drawingValue * 360f * 2,
                            pivot = spiral.getBounds().center
                        ) {
                            drawPath(
                                path = spiral,
                                color = Black,
                                style = Stroke(
                                    width = 4f
                                )
                            )
                        }


                    }
                }
            )

            val effectsValue by infinite.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 4000,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )

            Section(
                title = "Path Effects",
                visible = state.showEffect,
                onDraw = drawPathEffectTitle({ effectsValue })

            )

            val measureValue by infinite.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 4000,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
            Section(
                title = "Path Measuring",
                visible = state.showMeasure,
                onDraw = drawMeasureTitle(
                    progress = { measureValue }
                )
            )
        }
    }
}

@Composable
fun RowScope.Section(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String,
    onDraw: CacheDrawScope.() -> DrawResult = { onDrawBehind { } },
) {
    AnimatedContent(
        targetState = visible,
        modifier = modifier
            .padding(16.dp)
            .weight(1f)
            .fillMaxHeight(),
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it / 2 },
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            ) + fadeIn() togetherWith slideOutHorizontally(
                targetOffsetX = { it / 2 },
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            ) + fadeOut() using SizeTransform(clip = false)
        },

        ) {
        if (it)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(64.dp))
                    .drawWithCache(onDraw),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    fontSize = 32.sp
                )
            }
        else
            Box(Modifier.fillMaxSize())
    }
}