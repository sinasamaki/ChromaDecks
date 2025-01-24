package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.*
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.extensions.div
import com.sinasamaki.chromadecks.extensions.dpPx
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random


private val yellow = Color(0xffedc10e)
private val red = Color(0xffFF4444)
private val blue = Color(0xff1741e8)
private val orange = Color(0xffed3b18)
private val black = Color(0xff212121)
private val white = Color(0xffFFFFFF)
private val purple = Color(0xff8218ed)
private val green = Color(0xff42f55d)
private val paleYellow = Color(0xfffff5d9)


class InterpolationSlide : ListSlideSimple() {

    @Composable
    override fun content() {

        var topLeft by remember { mutableStateOf(Offset(0f, 0f)) }
        var topMiddle by remember { mutableStateOf(Offset(0.5f, 0f)) }
        var topRight by remember { mutableStateOf(Offset(1f, 0f)) }

        var middleLeft by remember { mutableStateOf(Offset(0f, .5f)) }
        var middleMiddle by remember { mutableStateOf(Offset(0.5f, .5f)) }
        var middleRight by remember { mutableStateOf(Offset(1f, .5f)) }

        var bottomLeft by remember { mutableStateOf(Offset(0f, 1f)) }
        var bottomMiddle by remember { mutableStateOf(Offset(0.5f, 1f)) }
        var bottomRight by remember { mutableStateOf(Offset(1f, 1f)) }

        val spec =
            remember { spring<Offset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow * .1f) }
        val animatedTopLeft by animateOffsetAsState(targetValue = topLeft, animationSpec = spec)
        val animatedTopMiddle by animateOffsetAsState(targetValue = topMiddle, animationSpec = spec)
        val animatedTopRight by animateOffsetAsState(targetValue = topRight, animationSpec = spec)

        val animatedMiddleLeft by animateOffsetAsState(targetValue = middleLeft, animationSpec = spec)
        val animatedMiddleMiddle by animateOffsetAsState(targetValue = middleMiddle, animationSpec = spec)
        val animatedMiddleRight by animateOffsetAsState(targetValue = middleRight, animationSpec = spec)

        val animatedBottomLeft by animateOffsetAsState(targetValue = bottomLeft, animationSpec = spec)
        val animatedBottomMiddle by animateOffsetAsState(targetValue = bottomMiddle, animationSpec = spec)
        val animatedBottomRight by animateOffsetAsState(targetValue = bottomRight, animationSpec = spec)

        LaunchedEffect(Unit) {
            while (true) {
                topLeft = Offset(nextPoint(), nextPoint())
                topMiddle = Offset(nextPoint(), nextPoint())
                topRight = Offset(nextPoint(), nextPoint())
                delay(Random.nextLong(from = 200L, until = 1000L))
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                middleLeft = Offset(nextPoint(), nextPoint())
                middleMiddle = Offset(nextPoint(), nextPoint())
                middleRight = Offset(nextPoint(), nextPoint())
                delay(Random.nextLong(from = 200L, until = 1000L))
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                bottomLeft = Offset(nextPoint(), nextPoint())
                bottomMiddle = Offset(nextPoint(), nextPoint())
                bottomRight = Offset(nextPoint(), nextPoint())
                delay(Random.nextLong(from = 200L, until = 1000L))
            }
        }
        Box(
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        topLeft = Offset(Random.nextFloat(), Random.nextFloat())
                        topMiddle = Offset(Random.nextFloat(), Random.nextFloat())
                        topRight = Offset(Random.nextFloat(), Random.nextFloat())

                        middleLeft = Offset(Random.nextFloat(), Random.nextFloat())
                        middleMiddle = Offset(Random.nextFloat(), Random.nextFloat())
                        middleRight = Offset(Random.nextFloat(), Random.nextFloat())

                        bottomLeft = Offset(Random.nextFloat(), Random.nextFloat())
                        bottomMiddle = Offset(Random.nextFloat(), Random.nextFloat())
                        bottomRight = Offset(Random.nextFloat(), Random.nextFloat())
                    }
                }
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val size = Size(600.dpPx, 600.dpPx)
            Box(
                Modifier
                    .meshGradient(
                        points = listOf(
                            listOf(
                                Offset(animatedTopLeft.x, animatedTopLeft.y) to black,
                                Offset(animatedTopMiddle.x, animatedTopMiddle.y) to black,
                                Offset(animatedTopRight.x, animatedTopRight.y) to black,
                            ),
                            listOf(
                                Offset(animatedMiddleLeft.x, animatedMiddleLeft.y) to blue,
                                Offset(animatedMiddleMiddle.x, animatedMiddleMiddle.y) to Fuchsia500,
                                Offset(animatedMiddleRight.x, animatedMiddleRight.y) to blue,
                            ),
                            listOf(
                                Offset(animatedBottomLeft.x, animatedBottomLeft.y) to green,
                                Offset(animatedBottomMiddle.x, animatedBottomMiddle.y) to green,
                                Offset(animatedBottomRight.x, animatedBottomRight.y) to green,
                            ),
                        ),
                        resolutionX = 20,
                        resolutionY = 20,
                    )
                    .size(600.dp)
            ) {

                ControlPoint(
                    offset = animatedTopLeft,
                    size = size,
                    setOffset = {
                        topLeft = it
                    }
                )

                ControlPoint(
                    offset = animatedTopMiddle,
                    size = size,
                    setOffset = {
                        topMiddle = it
                    }
                )

                ControlPoint(
                    offset = animatedTopRight,
                    size = size,
                    setOffset = {
                        topRight = it
                    }
                )





                ControlPoint(
                    offset = animatedMiddleLeft,
                    size = size,
                    setOffset = {
                        middleLeft = it
                    }
                )

                ControlPoint(
                    offset = animatedMiddleMiddle,
                    size = size,
                    setOffset = {
                        middleMiddle = it
                    }
                )

                ControlPoint(
                    offset = animatedMiddleRight,
                    size = size,
                    setOffset = {
                        middleRight = it
                    }
                )







                ControlPoint(
                    offset = animatedBottomLeft,
                    size = size,
                    setOffset = {
                        bottomLeft = it
                    }
                )

                ControlPoint(
                    offset = animatedBottomMiddle,
                    size = size,
                    setOffset = {
                        bottomMiddle = it
                    }
                )

                ControlPoint(
                    offset = animatedBottomRight,
                    size = size,
                    setOffset = {
                        bottomRight = it
                    }
                )

            }

//            Box(
//                Modifier
//                    .padding(128.dp)
//                    .aspectRatio(1f)
//                    .fillMaxSize()
//                    .meshGradient(
//                        points = listOf(
//                            listOf(
//                                Offset(0f, 0f) to purple,
//                                Offset(.5f, 0f) to orange,
//                                Offset(1f, 0f) to yellow,
//                            ),
//                            listOf(
//                                Offset(0f, 0.3f) to purple,
//                                Offset(.8f, 0.3f) to orange,
//                                Offset(1f, 0.3f) to yellow,
//                            ),
//                            listOf(
//                                Offset(0f, .7f) to purple,
//                                Offset(.2f, .7f) to orange,
//                                Offset(1f, .7f) to yellow,
//                            ),
//                            listOf(
//                                Offset(0f, 1f) to black,
//                                Offset(.5f, 1f) to black,
//                                Offset(1f, 1f) to black,
//                            ),
//                        ),
//                        stepsX = 15,
//                        stepsY = 15,
//                    )
//            )
        }
    }

    private fun nextPoint(): Float = (Random.nextFloat() + 0f) * 2f

    @Composable
    fun BoxScope.ControlPoint(
        modifier: Modifier = Modifier,
        offset: Offset = Offset.Zero,
        size: Size = Size(1f, 1f),
        setOffset: (Offset) -> Unit = {},
    ) {
        val offsetState by rememberUpdatedState(offset)
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        var isDragged by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (isHovered || isDragged) 1.2f else 1f
        )
        val width by animateDpAsState(
            targetValue = if (isHovered || isDragged) 32.dp else 8.dp
        )
        Box(
            modifier
                .size(32.dp)
                .offset(x = (-16).dp, y = (-16).dp)
                .offset { (offsetState * size).toIntOffset() }
                .scale(scale)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragged = true
                        },
                        onDragEnd = {
                            isDragged = false
                        }
                    ) { change, dragAmount ->
                        setOffset(offsetState + (dragAmount / size))
                    }
                }
                .hoverable(interactionSource)
                .pointerHoverIcon(PointerIcon.Hand)
                .border(
                    width = width,
//                    width = 8.dp,
                    color = White,
                    shape = CircleShape
                )

        )
    }
}


@Composable
fun AnimatedOffsetSlide(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var y by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }
    val animatedY by animateFloatAsState(
        targetValue = y,
        animationSpec = spring(
            stiffness = Spring.StiffnessVeryLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    Box(
        modifier
            .fillMaxSize()
            .onGloballyPositioned {
                y = it.boundsInWindow().top
                height = it.boundsInWindow().height
            }
    ) {
        Box(
            Modifier
                .offset { IntOffset(0, -height.roundToInt()) }
                .offset { IntOffset(0, (height - animatedY).roundToInt()) }
                .fillMaxWidth()
                .height(with(LocalDensity.current) { height.toDp() }),
        ) {
            content()
        }
    }

}


