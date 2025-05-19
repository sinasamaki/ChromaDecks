package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.polarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.ui.theme.Blue700
import com.sinasamaki.chromadecks.ui.theme.Yellow200

internal class PromoTest2 : ListSlideSimple() {

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

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Blue700)
                .graphicsLayer {
//                    rotationX = 30f
                }
                .drawWithCache {
                    val shape1 = Path()

                    shape1.moveTo(size.width / 2f, size.height / 2f)

                    for (i in 0..5) {
                        shape1.relativePolarLineTo(350f, 0f + (i * 40f))
                    }


                    val shape2 = Path()
                    shape2.moveTo(size.width / 2f, size.height / 2f)
                    for (i in 0..8_000) {
                        shape2.polarLineTo(
                            degrees = (i * 1f) % 360f,
                            distance = 0f + (i * .3f),
                            origin = Offset(size.width / 2f, size.height / 2f)
                        )
                    }

                    val measure = PathMeasure()
                    measure.setPath(shape2, false)

                    println("new path")

                    onDrawBehind {
                        //                    drawPath(
//                        shape1,
//                        color = Yellow400,
//                        style = Stroke(
//                            width = 2f,
////                            join = StrokeJoin.Round
//                        )
//                    )

                        rotate(
                            degrees = lerp(0f, -360f, rotation * 30),
                            pivot = center
                        ) {
                            drawPath(
                                shape2,
                                color = Yellow200,
                                style = Stroke(
                                    width = 10.dp.toPx(),
                                    join = StrokeJoin.Round,
                                    cap = StrokeCap.Round,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(
                                            measure.length * rotation,
                                            measure.length,
                                        )
                                    )
                                )
                            )
                        }
                    }


                }
        ) {}

    }

}