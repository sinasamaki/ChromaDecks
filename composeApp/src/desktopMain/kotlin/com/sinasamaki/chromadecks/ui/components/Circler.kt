package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Emerald500
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.PI
import kotlin.random.Random


@Composable
fun Modifier.circler(
    color: Color = Lime500
): Modifier {

    val scope = rememberCoroutineScope()
    val circleAlpha = remember { Animatable(0f) }
    val circleAnimation = remember { Animatable(0f) }
    val circleRotation = remember { Animatable(0f) }
    val rotation = remember {
        45f * Random.nextInt(10)
    }
    return this
        .pointerInput(Unit) {

            detectTapGestures {
                scope.launch {
                    circleAlpha.snapTo(1f)
                    circleAnimation.snapTo(0f)
                    circleRotation.snapTo(rotation)
                    launch {
                        circleRotation.animateTo(rotation + 45, tween(1_300))
                    }
                    circleAnimation.animateTo(1f, tween(1000))
                    delay(300)
                    circleAlpha.animateTo(0f, tween(500))
                }
            }
        }
        .drawBehind {
            rotate(
                circleRotation.value
            ) {
                drawCircle(
                    color = color.copy(alpha = circleAlpha.value),
                    radius = size.width * 1.1f,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                (size.width * 1.1f * PI * 2 * circleAnimation.value).toFloat(),
                                (size.width * 1.1f * PI * 2).toFloat(),
                            )
                        )
                    )
                )
            }
        }
}