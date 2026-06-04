package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PillAnimationSlideState
class PillAnimationSlide : ListSlideAdvanced<PillAnimationSlideState>() {

    override val initialState: PillAnimationSlideState
        get() = PillAnimationSlideState()

    @Composable
    override fun content(state: PillAnimationSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "PillAnimation.kt" to """
                    LaunchedEffect(Unit) {
                        while (true) {
                            pillRotation.animateTo(
                                -10f, animationSpec = spring(
                                    stiffness = Spring.StiffnessLow,
                                )
                            )
                            pillRotation.animateTo(
                                10f, animationSpec = spring(
                                    stiffness = Spring.StiffnessMedium,
                                )
                            )
                            ...
                            delay(400)
                        }
                    }
                """.trimIndent()
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                val pillRotation = remember { Animatable(0f) }
                val pillTranslation = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    while (true) {
                        pillRotation.animateTo(
                            -10f, animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                            )
                        )
                        pillRotation.animateTo(
                            10f, animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                            )
                        )
                        pillRotation.animateTo(
                            -10f, animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                            )
                        )
                        pillRotation.animateTo(
                            10f, animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                            )
                        )
                        pillRotation.animateTo(
                            0f, animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                            )
                        )
                        delay(400)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .graphicsLayer {
                            translationX =
                                (size.width - size.height) * pillTranslation.value
                            transformOrigin = TransformOrigin(
                                pivotFractionX = (size.height / 2) / size.width,
                                pivotFractionY = .5f,
                            )
                            rotationZ = pillRotation.value
                        }
                        .background(
                            color = White,
                            shape = CircleShape,
                        )
                ) {
                    val rotation by rememberInfiniteTransition(label = "asterisk")
                        .animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 15_000,
                                    easing = LinearEasing,
                                ),
                                repeatMode = RepeatMode.Restart,
                            ),
                            label = "asterisk-rotation",
                        )
                    Canvas(
                        modifier = Modifier
                            .padding(end = 32.dp)
                            .align(
                                BiasAlignment(
                                    horizontalBias = 1f,
                                    verticalBias = 0f,
                                )
                            )
                            .fillMaxHeight(.7f)
                            .aspectRatio(1f)
                            .rotate(rotation)
                    ) {
                        val arms = 6
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val outerR = size.minDimension / 2f
                        val innerR = outerR * .28f
                        val halfWidth = outerR * .18f
                        val path = Path()
                        repeat(arms) { i ->
                            val angle = (i * (2.0 * PI / arms) - PI / 2.0)
                            val dirX = cos(angle).toFloat()
                            val dirY = sin(angle).toFloat()
                            val tipX = cx + outerR * dirX
                            val tipY = cy + outerR * dirY
                            val corner1X = tipX + halfWidth * dirY
                            val corner1Y = tipY - halfWidth * dirX
                            val corner2X = tipX - halfWidth * dirY
                            val corner2Y = tipY + halfWidth * dirX
                            val notchAngle = angle + PI / arms
                            val notchX = cx + innerR * cos(notchAngle).toFloat()
                            val notchY = cy + innerR * sin(notchAngle).toFloat()
                            if (i == 0) path.moveTo(corner1X, corner1Y)
                            else path.lineTo(corner1X, corner1Y)
                            path.lineTo(corner2X, corner2Y)
                            path.lineTo(notchX, notchY)
                        }
                        path.close()
                        drawPath(path = path, color = Zinc900)
                    }
                }
            }
        }
    }
}
