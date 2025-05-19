package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.cubicTo
import com.sinasamaki.chromadecks._002_PathAnimations.drawScaledPath
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.quadraticTo
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.div
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.components.circler
import com.sinasamaki.chromadecks.ui.theme.Orange300
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Sky100
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate100
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate900
import com.sinasamaki.chromadecks.ui.theme.Stone950
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

//private val background = Slate50
//private val path = Slate950
//private val controlPointColor = Red500

private val background = Zinc900
private val pathColor = Sky400
private val pathColorAlt = Pink400
private val controlPointColor = Sky100

internal data class QuadraticCubicState(
    val showArguments: Boolean = true
)

internal class QuadraticCubic : ListSlideAdvanced<QuadraticCubicState>() {

    override val initialState: QuadraticCubicState
        get() = QuadraticCubicState()

    override val stateMutations: List<QuadraticCubicState.() -> QuadraticCubicState>
        get() = listOf()

    @Composable
    override fun content(state: QuadraticCubicState) {

        var y by remember { mutableFloatStateOf(3000f) }
        val animatedY by animateFloatAsState(
            targetValue = y,
            animationSpec = tween(3000)
        )

        val slideState = LocalSlideState.current
        LaunchedEffect(slideState) {
            delay(16)
            y = when {
                slideState.slideIndex == slideState.currentIndex -> 0f
                slideState.slideIndex < slideState.currentIndex -> 0f
                else -> 2000f
            }
        }
        Row(
            modifier = Modifier
                .background(Slate100)
                .clip(RectangleShape)
                .graphicsLayer {
                    val path = Path()

                    path.moveTo(size.width, size.height * 2f)
                    path.lineTo(0f, size.height * 2f)
                    path.lineTo(0f, 0f)
                    path.cubicTo(
                        x1 = size.width * .25f, y1 = -size.height * .25f,
                        x2 = size.width * .75f, y2 = size.height * .25f,
                        x3 = size.width, y3 = 0f
                    )

                    path.translate(Offset(0f, animatedY - size.height * .5f))

                    clip = true
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Generic(path)
                        }

                    }
                }
                .background(background)
                .padding(top = 64.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {

            var quadraticStart = remember { mutableStateOf(Offset(10f, 60f)) }
            var quadraticControl1 = remember { mutableStateOf(Offset(50f, 20f)) }
            var quadraticEnd = remember { mutableStateOf(Offset(90f, 60f)) }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 32.dp),
            ) {
                PathDemoGrid(
                    pointStates = listOf(quadraticStart, quadraticControl1, quadraticEnd)
                )

                Spacer(Modifier.height(32.dp))

                CodeBlock(
                    code = "path.quadraticTo(" +
                            (if (state.showArguments) {
                                "\n\tx1 = ${quadraticControl1.value.x.toInt()}, " +
                                        "y1 = ${quadraticControl1.value.y.toInt()},\n" +
                                        "\tx2 = ${quadraticEnd.value.x.toInt()}, " +
                                        "y2 = ${quadraticEnd.value.y.toInt()},\n"
                            } else "") +
                            ")",
                    enableAnimations = false,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            var cubicStart = remember { mutableStateOf(Offset(10f, 60f)) }
            var cubicControl1 = remember { mutableStateOf(Offset(25f, 20f)) }
            var cubicControl2 = remember { mutableStateOf(Offset(70f, 80f)) }
            var cubicEnd = remember { mutableStateOf(Offset(90f, 60f)) }


            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 32.dp),
            ) {
                PathDemoGrid(
                    pointStates = listOf(cubicStart, cubicControl1, cubicControl2, cubicEnd)
                )

                Spacer(Modifier.height(32.dp))

                CodeBlock(
                    code = "path.cubicTo(" +
                            (if (state.showArguments) {
                                "\n\tx1 = ${cubicControl1.value.x.toInt()}, " +
                                        "y1 = ${cubicControl1.value.y.toInt()},\n" +
                                        "\tx2 = ${cubicControl2.value.x.toInt()}, " +
                                        "y2 = ${cubicControl2.value.y.toInt()},\n" +
                                        "\tx3 = ${cubicEnd.value.x.toInt()}, " +
                                        "y3 = ${cubicEnd.value.y.toInt()},\n"
                            } else "") +
                            ")",
                    enableAnimations = false,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    }
}


@Composable
private fun PathDemoGrid(
    modifier: Modifier = Modifier,
    pointStates: List<MutableState<Offset>>,
) {
    LaunchedEffect(Unit) { if (pointStates.size !in 3..4) error("Only 3 or 4 control points needed") }

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = background
            )
            .drawCartesianGrid(color = pathColor, range = 10, alpha = .2f)
            .drawCartesianGrid(color = pathColor, range = 100, alpha = .05f)
            .drawBehind {

                val path = Path()
                val controlPath = Path()

                path.moveTo(pointStates[0].value)

                when (pointStates.size) {
                    3 -> {
                        path.quadraticTo(
                            pointStates[1].value,
                            pointStates[2].value,
                        )
                        controlPath.moveTo(pointStates[0].value)
                        controlPath.lineTo(pointStates[1].value)
                        controlPath.moveTo(pointStates[2].value)
                        controlPath.lineTo(pointStates[1].value)
                    }

                    4 -> {
                        path.cubicTo(
                            pointStates[1].value,
                            pointStates[2].value,
                            pointStates[3].value,
                        )
                        controlPath.moveTo(pointStates[0].value)
                        controlPath.lineTo(pointStates[1].value)
                        controlPath.moveTo(pointStates[2].value)
                        controlPath.lineTo(pointStates[3].value)
                    }
                }



                drawScaledPath(
                    path = path,
                    brush = Brush.horizontalGradient(
                        colors = listOf(pathColor, pathColorAlt)
                    ),
                    style = Stroke(
                        width = 10.dp.toPx()
                    )
                )

                drawScaledPath(
                    path = controlPath,
                    color = controlPointColor,
                    alpha = .3f,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                8.dp.toPx(),
                                4.dp.toPx(),
                            )
                        )
                    )
                )
            }
    ) {
        pointStates.forEach { pointState ->
            PointHandle(
                pointState = pointState,
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxWithConstraintsScope.PointHandle(
    modifier: Modifier = Modifier,
    pointState: MutableState<Offset>,
) {

    val width = maxWidth.toPx
    val height = maxHeight.toPx
    val boxSize = Size(width, height)

    val interaction = remember { MutableInteractionSource() }
    val isHovered by interaction.collectIsHoveredAsState()

    var isDragged by remember { mutableStateOf(false) }


    Box(
        modifier = modifier
            .align(Alignment.TopStart)
    ) {
        val scale by animateFloatAsState(
            targetValue = if (isHovered || isDragged) 1f else .8f
        )
        Box(
            modifier = modifier
                .offset {
                    IntOffset(
                        ((pointState.value.x / 100f) * width).roundToInt(),
                        ((pointState.value.y / 100f) * height).roundToInt(),
                    )
                }
                .size(20.dp)
                .offset((-10).dp, (-10).dp)
                .pointerInput(boxSize) {
                    detectDragGestures(
                        onDragStart = { isDragged = true },
                        onDragEnd = { isDragged = false }
                    ) {
                        pointState.value += ((it.div(boxSize)) * 100f)
                    }

                }
                .hoverable(interaction)
                .pointerHoverIcon(PointerIcon.Hand)
                .scale(scale)
                .circler(Orange600)
                .background(controlPointColor, CircleShape)
        )

//        if (isHovered || isDragged)
        Column(
            modifier = Modifier
                .width(100.dp)
                .height(80.dp)
                .offset {
                    IntOffset(
                        ((pointState.value.x / 100f) * width).roundToInt(),
                        ((pointState.value.y / 100f) * height).roundToInt(),
                    )
                }
                .offset(x = -50.dp, y = (-80).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "${pointState.value.x.roundToInt()},${pointState.value.y.roundToInt()}",
                modifier = Modifier.background(
                    color = background.copy(alpha = .7f),
                    shape = CircleShape,
                )
                    .padding(4.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                color = controlPointColor,
            )

            Spacer(Modifier.height(8.dp))

            Box(
                Modifier
                    .height(100.dp)
                    .width(10.dp)
                    .drawBehind {
                        val path = Path()
                        path.moveTo(x = size.width / 2, y = 0f)
                        path.lineTo(x = size.width / 2, size.height)
                        drawPath(
                            path = path,
                            color = controlPointColor,
                            style = Stroke(
                                width = 2.dp.toPx()
                            )
                        )
                    }
            )

        }
    }

}


