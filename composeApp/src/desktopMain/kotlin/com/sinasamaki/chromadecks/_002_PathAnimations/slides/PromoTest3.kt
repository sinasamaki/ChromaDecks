package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.createSpring
import com.sinasamaki.chromadecks._002_PathAnimations.drawArrowOnPath
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.spring
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.extensions.pxDp
import com.sinasamaki.chromadecks.ui.theme.Cyan400
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Slate200
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.random.Random

internal class PromoTest3 : ListSlideSimple() {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun content() {

        val progress = remember { Animatable(1f) }

        val infinite = rememberInfiniteTransition()

        val rotation by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 20_000,
                    easing = FastOutSlowInEasing,
                )
            )
        )

        val arrowProgress by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000)
            )
        )


        BoxWithConstraints(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Zinc900)
                .graphicsLayer {
//                    rotationX = 30f
                }
                .drawWithCache {


                    val arrowPath = Path()
                    arrowPath.moveTo(400f, 1500f)
                    arrowPath.lineTo(1500f, 1500f)

                    val arrowPath2 = createSpring(
                        start = Offset(2000f, 2000f),
                        end = Offset(2000f, 800f),
                        radius = 50f,
                        loops = 20f
                    )

//                    arrowPath2.moveTo(2000f, 2000f)
//
//                    for (i in 0..10) {
//                        arrowPath2.relativePolarLineTo(
//                            120.7f,
//                            distance = 0f + (i * 4f),
//
//                        )
//                    }

//                    arrowPath2.lineTo(2000f, 800f)


                    val shape1 = Path()



                    shape1.moveTo(size.width / 2f, size.height / 2f)

                    for (i in 0..5) {
                        shape1.relativePolarLineTo(350f, 0f + (i * 40f))
                    }


                    val shape2 = Path()
                    shape2.moveTo(size.width / 2f, size.height / 2f)
                    for (i in 0..191) {
                        shape2.relativePolarLineTo(
//                            degrees = (i * 1f) % 360f,
                            120.7f,
//                            distance = 0f + ((if (i == 20) i-1 else i )* 4f),
                            distance = 0f + (i * 4f),
//                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }

                    val measure = PathMeasure()
                    measure.setPath(shape2, false)


                    println("new path")

                    val spiral1 = createSpring(
                        start = Offset(1000f, 1000f),
                        end = Offset(500f, 500f),
                        radius = 100f,
                    )

                    val loops = 4.5f
                    val spiral2 = createSpring(
                        start = Offset(500f, 150f),
                        end = Offset(1000f, 150f),
                        radius = 100f + 10f,
                        startAngle = -90f,
                        loops = loops,
                    )

                    val spiralMeasure = PathMeasure()
                    spiralMeasure.setPath(spiral2, false)

                    println("measure = ${spiralMeasure.length} || calc = ${PI * 200f * loops}")

                    onDrawBehind {


                        drawPath(
                            path = spiral1,
                            color = Red500,
                            style = Stroke(
                                width = 1f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                            )
                        )


                        drawPath(
                            path = spiral2,
                            color = Lime200.copy(alpha = .8f),
                            style = Stroke(width = 1f)
                        )
                        drawPath(
                            path = spiral2,
                            color = Yellow300,
                            style = Stroke(
                                width = 10f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        spiralMeasure.length / ((loops * 2) + 1),
                                        spiralMeasure.length / ((loops * 2) + 1),
                                    )
                                )
                            )
                        )
                        drawRoundRect(
                            color = Slate50.copy(alpha = 1f),
                            topLeft = Offset(500f, 50f),
                            size = Size(
                                width = 500f, height = 200f,
                            ),
                            cornerRadius = CornerRadius(150f)
                        )
                        drawPath(
                            path = spiral2,
                            color = Yellow300,
                            style = Stroke(
                                width = 10f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        spiralMeasure.length / ((loops * 2) + 1),
                                        spiralMeasure.length / ((loops * 2) + 1),
                                    ),
                                    phase = -(spiralMeasure.length / ((loops * 2) + 1))
                                )
                            )
                        )


                        drawRect(color = Color.Black)

                        val measure = PathMeasure()
                        measure.setPath(arrowPath, false)
                        drawPath(
                            path = arrowPath,
                            color = Slate200,
                            style = Stroke(
                                width = 50f,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        measure.length * arrowProgress, measure.length
                                    )
                                )

                            )
                        )

                        drawArrowOnPath(
                            path = arrowPath,
                            progress = arrowProgress,
                        )


                        val measure2 = PathMeasure()
                        measure2.setPath(arrowPath2, false)

                        drawPath(
                            path = arrowPath2,
                            color = Slate200,
                            style = Stroke(
                                width = 5f,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        measure2.length * arrowProgress, measure2.length
                                    )
                                )

                            )
                        )

                        drawArrowOnPath(
                            path = arrowPath2,
                            progress = arrowProgress,
                        )


//                        rotate(
//                            degrees = lerp(0f, 360f, rotation * 1),
//                            pivot = center
//                        ) {
//                            drawPath(
//                                shape2,
//                                color = Yellow600,
//                                style = Stroke(
//                                    width = 1.dp.toPx(),
//                                    pathEffect = PathEffect.chainPathEffect(
//                                        PathEffect.dashPathEffect(
//                                            intervals = floatArrayOf(
//                                                measure.length * rotation,
//                                                measure.length,
//                                            )
//                                        ),
//                                        PathEffect.cornerPathEffect(
//                                            radius = 1.dp.toPx()
//                                        ),
//                                    )
//                                )
//                            )
//                        }
                    }


                }
        ) {
            val infinite = rememberInfiniteTransition()
            val progress by infinite.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5000)
                )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {


                val text = remember { "Hallo Welt" }
                val style = remember {
                    TextStyle(
                        color = Slate50,
                        fontSize = 64.sp,

                        )
                }

                val textMeasurer = rememberTextMeasurer()
                val result = textMeasurer.measure(text = text, style = style)

                val points = remember {
                    buildList {
                            add(Offset(1000f, 0f))
                        for (i in 0..2) {
                            add(
                                Offset(
                                    Random.nextInt(-500, 500).toFloat(),
                                    Random.nextInt(-500, 500).toFloat(),
                                )
                            )
                        }
                    }
                }

                Box(
                    Modifier
                        .width(result.size.width.pxDp)
                        .height(result.size.height.pxDp)
//                        .background(Red500.copy(alpha = .4f))
                        .drawBehind {

                            val path = Path()
                            path.moveTo(
                                result.getBoundingBox(0).left,
                                result.getBoundingBox(0).bottom,
                            )

                            path.lineTo(
                                result.getBoundingBox(text.lastIndex).right,
                                result.getBoundingBox(text.lastIndex).bottom,
                            )

                            path.relativeQuadraticTo(
                                300f, 0f,
                                300f, 200f
                            )
                            path.relativeQuadraticTo(
                                0f, 450f,
                                -900f, 200f,
                            )


                            path.relativeCubicTo(
                                -300f, -100f,
                                -200f, -700f,
                                100f, -700f,
                            )

                            path.relativeQuadraticTo(
                                0f, 0f,
                                result.size.width.toFloat(), 0f,
                            )


                            path.reset()
                            path.addPath(
                                createPath(
                                    buildList {
                                        add(result.getBoundingBox(0).bottomLeft)
                                        add(result.getBoundingBox(text.lastIndex).bottomRight)
                                        addAll(points)
                                    }
                                ).first
                            )


//                            val path = result.getPathForRange(0, text.length)

                            drawPath(
                                path = path,
                                color = Cyan400,
                                style = Stroke(width = 10f)
                            )

                            val measure = PathMeasure()
                            measure.setPath(path, false)

                            val origin = result.getBoundingBox(0).topLeft
                            val end = result.getBoundingBox(text.lastIndex).bottomRight

                            text.forEachIndexed { index, char ->
                                val rect = result.getBoundingBox(index)
                                val distance = rect.left + ((measure.length - end.x) * progress)
                                val pathOffset = measure.getPosition(
                                    distance
                                )
                                val rotation = measure.getTangent(distance).let {
                                    (atan2(it.y, it.x) * (180 / PI)).toFloat()
                                }

                                translate(
                                    left = pathOffset.x - rect.left,
                                    top = pathOffset.y - rect.height,
                                ) {
                                    rotate(
                                        degrees = rotation,
                                        pivot = rect.bottomLeft
                                    ) {

                                        drawText(
                                            textMeasurer = textMeasurer,
                                            text = char.toString(),
                                            style = style,
                                            topLeft = rect.topLeft
                                        )
                                    }
                                }
                            }

                        }
                )

                Spacer(Modifier.height(32.dp))


                Button(
                    onClick = {},
                    modifier = Modifier
                        .spring(
                            color = Yellow300,
                            stroke = 4.dp,
                            progress = progress * 2f,
                            loops = 2
                        )
                        .spring(
                            color = Pink300,
                            stroke = 4.dp,
                            progress = progress,
                            loops = 1,
                            flip = true,
                        )
                        .padding(vertical = 10.dp)
                        .shadow(elevation = 10.dp)
                ) {
                    Text("Hallo Welt", fontSize = 32.sp)
                }
            }
        }

    }

}