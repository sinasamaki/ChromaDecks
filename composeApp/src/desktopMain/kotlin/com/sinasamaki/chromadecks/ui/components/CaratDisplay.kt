package com.sinasamaki.chromadecks.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.util.StepsEasing
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

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
                                val distFromBorder = kotlin.math.abs(dist - circle.radius) * 4f
                                1f - (distFromBorder / circle.radius).coerceIn(0f, 1f)
                            } ?: 0f
                        )

                        val scale = 0f + influence * ((index % 12f) / 12f) * 1.4f
                        val shapeSize = minOf(cellW, cellH) * scale
                        val half = shapeSize / 2f

                        val color = colors.random(Random(row * 31 + col))

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

                                1 -> {
                                    drawRect(
                                        color = color.copy(alpha = .3f),
                                        topLeft = Offset(cx - half, cy - half),
                                        size = Size(shapeSize, shapeSize),
                                    )
                                    drawRect(
                                        color = color,
                                        topLeft = Offset(cx - half, cy - half),
                                        size = Size(shapeSize, shapeSize),
                                        style = Stroke(width = 2f),
                                    )
                                }

                                2 -> {
                                    val path = Path()
                                    path.moveTo(cx, cy - half)
                                    path.lineTo(cx + half, cy + half)
                                    path.lineTo(cx - half, cy + half)
                                    path.close()
                                    drawPath(
                                        path = path,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                color.copy(alpha = .1f),
                                                color.copy(alpha = .7f)
                                            ),
                                            startY = cy,
                                            endY = cy + shapeSize,
                                        )
                                    )
                                    drawPath(
                                        path = path,
                                        color = color,
                                        style = Stroke(width = 1f),
                                    )
                                }
                            }
                        }
                    }
                }
            }
    )
}
