package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal data class OutroSlideState(val placeholder: Unit = Unit)

internal class OutroSlide : ListSlideAdvanced<OutroSlideState>() {

    override val initialState get() = OutroSlideState()

    @Composable
    override fun content(state: OutroSlideState) {
        val radius = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                radius.snapTo(0f)
                radius.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
                )
                delay(500)
            }
        }
        CaratDisplay(
            Modifier
                .fillMaxSize(),
            aspectRatio = 3 / 4f,
            colors = listOf(
                Zinc300, Lime400, Green400,
            )
        ) {
            listOf(
//                InfluenceCircle(
//                    it.center + Offset(-500f * radius.value, 0f),
//                    radius = it.width * radius.value
//                ),
                InfluenceCircle(
                    Offset(it.width, it.height * (1f - radius.value)),
                    radius = it.width * (1f - radius.value)
                ),
                InfluenceCircle(
                    Offset(0f, it.height * (1f - radius.value)),
                    radius = it.width * (1f - radius.value)
                ),
                InfluenceCircle(
                    Offset(it.center.x, it.center.y + it.center.y * (radius.value)),
                    radius = it.width * (1f - radius.value)
                ),
            )
        }

        TitleFrame(
            modifier = Modifier.fillMaxSize(),
            title = "chromadial",
            description = "custom dial controls with\nsmooth animations and haptic feedback",
            hint = "chromadial.sinasamaki.com",
            bookNumber = 3,
            animationProgress = radius.value,
            contentColor = Zinc50,
        )

    }
}

fun StepsEasing(steps: Int) = Easing { fraction ->
    (fraction * steps).toInt() / steps.toFloat()
}

data class InfluenceCircle(
    val center: Offset,
    val radius: Float,
)

@Composable
fun CaratDisplay(
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    resolution: Int = 40,
    colors: List<Color> = listOf(Zinc50),
    circles: (size: Size) -> List<InfluenceCircle> = { emptyList() },
) {
    Box(
        modifier = modifier
            .drawBehind {
                val cols = resolution
                val rows = (resolution / aspectRatio).roundToInt().coerceAtLeast(1)
                val cellW = size.width / cols
                val cellH = size.height / rows
                val influenceCircles = circles(size)

                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val cx = col * cellW + cellW / 2f
                        val cy = row * cellH + cellH / 2f
                        val center = Offset(cx, cy)
                        val index = col * rows + row

                        val influence = StepsEasing(4).transform(
                            influenceCircles.maxOfOrNull { circle ->
                                val dx = center.x - circle.center.x
                                val dy = center.y - circle.center.y
                                val dist = sqrt(dx * dx + dy * dy) + ((index % 3f) / 3f) * 100f
                                val distFromBorder = kotlin.math.abs(dist - circle.radius) * 2f
                                1f - (distFromBorder / circle.radius).coerceIn(0f, 1f)
                            } ?: 0f
                        )

                        val scale = 0f + influence * ((index % 12f) / 12f) * 2f
                        val shapeSize = minOf(cellW, cellH) * scale
                        val half = shapeSize / 2f

                        val color = colors.random()

                        translate(
                            left = when (row % 2 == 0) {
                                true -> -1
                                false -> 1
                            } * cellW * .25f
                        ) {
                            when (index % 3) {
                                0 -> {
                                    drawCircle(color = color, radius = half, center = center)
                                    if (index % 6 == 0) {
                                        drawCircle(
                                            color = color,
                                            radius = half * 1.5f,
                                            center = center,
                                            style = Stroke(
                                                width = 2f,
                                            )
                                        )
                                    }
                                }

                                1 -> drawRect(
                                    color = color,
                                    topLeft = Offset(cx - half, cy - half),
                                    size = Size(shapeSize, shapeSize),
                                )

                                2 -> {
                                    val path = Path()
                                    path.moveTo(cx, cy - half)
                                    path.lineTo(cx + half, cy + half)
                                    path.lineTo(cx - half, cy + half)
                                    path.close()
                                    drawPath(path = path, color = color)
                                }
                            }
                        }
                    }
                }
            }
    )
}
