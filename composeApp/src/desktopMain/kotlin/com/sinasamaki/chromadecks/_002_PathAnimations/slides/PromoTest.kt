package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.isTypedEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.extensions.div
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.theme.Amber50
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Blue700
import com.sinasamaki.chromadecks.ui.theme.Green300
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Green950
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Pink100
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Pink950
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Purple600
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.Rose600
import com.sinasamaki.chromadecks.ui.theme.Rose800
import com.sinasamaki.chromadecks.ui.theme.Sky200
import com.sinasamaki.chromadecks.ui.theme.Slate100
import com.sinasamaki.chromadecks.ui.theme.Slate300
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate800
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.Teal200
import com.sinasamaki.chromadecks.ui.theme.Teal500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow200
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Yellow50
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import org.jetbrains.skiko.currentNanoTime
import kotlin.random.Random

internal class PromoTest : ListSlideSimple() {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun content() {

        val progress = remember { Animatable(1f) }
        var unitOffsets by remember {
            mutableStateOf(
                listOf(
                    Offset(
                        Random.nextFloat(),
                        Random.nextFloat()
                    ), Offset(Random.nextFloat(), Random.nextFloat())
                )
            )
        }
        LaunchedEffect(currentNanoTime()) {
            unitOffsets = listOf(
                Offset(.4f, .1f),
                Offset(.8f, .5f),
                Offset(.3f, .3f),
                Offset(.8f, .8f),
                Offset(.4f, .7f),
                Offset(.2f, .9f),
            )
            while (true) {

//                val random = buildList {
//                    add(unitOffsets.last())
//                    for (i in 0..6) {
//                        add(
//                            Offset(
//                                x = Random.nextFloat(),
//                                y = Random.nextFloat(),
//                            )
//                        )
//                    }
//                }
//
//                unitOffsets = random

                val duration = 3_000
                progress.animateTo(
                    1f,
                    animationSpec = tween(duration, easing = FastOutSlowInEasing)
                )
                progress.animateTo(
                    2f,
                    animationSpec = tween(duration, easing = FastOutLinearInEasing)
                )
                progress.snapTo(0f)
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Zinc50)
                .drawBehind {

                    val showControlPoints = false
                    val radius = 250f
                    val paintBrushTexture = false


                    val (path, controlPoints) = createPath(unitOffsets.map { it * size })

                    val measure = PathMeasure()
                    measure.setPath(path, false)

                    val num = (.2f * measure.length).toInt()
                    for (i in 0..num) {
                        val frac = i.toFloat() / num

                        if (progress.value > 1f) {
                            if (frac < (progress.value - 1f)) continue
                        } else {
                            if (frac > progress.value) continue
                        }
                        val distance = (frac) * measure.length
                        val position = measure.getPosition(distance)

//                        val range = listOf(Orange600, Yellow100, Lime500, Indigo700, Orange600)
                        val range = listOf(

//                            Blue700,
//                            Sky200,
//                            Teal500,

//                            Green500,
//                            Yellow300,
//                            Orange400,

//                            Red600,
//                            Yellow200,
//                            Zinc950,

                            Black,
                            Zinc50,
                            Rose600,

//                            Blue700,
//                            Green400,
//                            Yellow300,
//                            Amber50,
//                            Orange400,
//                            Red500,
//                            Rose800,
//                            Pink950,

//                            Violet800,
//                            Blue700,
                        )
                        val interval = 1f / range.lastIndex

                        val start = (frac * range.lastIndex).toInt()
                        val end = (frac * range.lastIndex).toInt() + 1

                        val color1 = range.getOrElse(start, defaultValue = { range.last() })
                        val color2 = range.getOrElse(end, defaultValue = { range.last() })

                        val color = lerp(color1, color2, (frac % interval) / interval)


//                        val color = lerp(Orange600, Blue500, frac)
                        drawCircle(
                            brush = if (paintBrushTexture) object : ShaderBrush() {
                                override fun createShader(size: Size): Shader {
                                    return RadialGradientShader(
                                        center = position,
                                        radius = radius,
                                        colors = listOf(
                                            color,
                                            color.copy(alpha = .61f),
                                            color.copy(alpha = 0f),
                                        ),
                                        colorStops = listOf(0f, .05f, .5f)
                                    )
                                }
                            } else Brush.verticalGradient(
                                colors = listOf(
                                    lerp(color, Slate50, .2f),
                                    color,
                                    lerp(color, Slate950, .2f),
                                ),
                                startY = position.y - radius,
                                endY = position.y + radius
                            ),
//                            color = color,
//                            style = Stroke(
//                                width = 60f,
//                            ),
//                            alpha = frac / progress.value,
                            center = position,
                            radius = radius,
                        )
                    }

                    if (showControlPoints) {
                        drawPath(
                            path = path,
                            color = Pink950,
                            style = Stroke(
                                width = 4f,
                                join = StrokeJoin.Round,
                            )
                        )
                        drawPath(
                            path = path,
                            color = Pink300,
                            style = Stroke(
                                width = 2f,
                                join = StrokeJoin.Round,
                            )
                        )

                        controlPoints.forEach { (first, second) ->
                            drawLine(
                                color = Green400,
                                strokeWidth = 4f,
                                start = first,
                                end = second,
                            )
                            listOf(first, second).forEach {
                                drawCircle(
                                    color = Green950,
                                    center = it,
                                    radius = 14f,
                                )
                                drawCircle(
                                    color = Green400,
                                    center = it,
                                    radius = 10f,
                                )
                            }
                        }
                    }

                }
        ) {

            val density = LocalDensity.current
            val size = Size(
                width = with(density) { maxWidth.toPx() },
                height = with(density) { maxHeight.toPx() }
            )
            var alpha by remember { mutableStateOf(true) }
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable {
                        alpha = !alpha
                    }
            )
            unitOffsets.forEachIndexed { index, offset ->

                val pointSize = remember { 100.dp }
                key(
                    index
                ) {
                    Box(
                        Modifier
                            .offset { (offset * size).toIntOffset() }
                            .offset(x = -pointSize / 2, y = -pointSize / 2)
                            .alpha(if(alpha) 1f else 0f)
                            .border(
                                width = 4.dp,
                                color = Slate800,
                                shape = CircleShape
                            )
                            .background(
                                color = Slate100,
                                shape = CircleShape
                            )
                            .size(pointSize)
                            .pointerInput(Unit) {

                                detectDragGestures(
                                    onDrag = { deltaOffset ->
                                        unitOffsets =
                                            unitOffsets.mapIndexed { draggedIndex, draggedOffset ->

                                                if (index == draggedIndex) {
                                                    draggedOffset + (deltaOffset / size)
                                                } else {
                                                    draggedOffset
                                                }

                                            }
                                    }
                                )

                            }
                    )
                }

            }

        }

    }

}
