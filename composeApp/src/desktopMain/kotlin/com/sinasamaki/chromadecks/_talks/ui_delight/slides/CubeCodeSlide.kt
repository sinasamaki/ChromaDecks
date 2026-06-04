package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._talks.ui_delight.components.Cube
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Purple300
import com.sinasamaki.chromadecks.ui.theme.Purple500
import kotlin.random.Random

class CubeCodeSlideState
class CubeCodeSlide : ListSlideAdvanced<CubeCodeSlideState>() {

    override val initialState: CubeCodeSlideState
        get() = CubeCodeSlideState()

    @Composable
    override fun content(state: CubeCodeSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "Cube.kt" to """
                    var rotationY by remember { mutableStateOf(0f) }

                    Cube(
                        modifier = Modifier
                            .size(300.dp),
                        angleX = -20f,
                        angleY = rotationY,
                    ) { face ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Lime500, Purple500, Purple300,
                                        )
                                    ),
                                    shape = RectangleShape,
                                )
                        )
                    }
                """.trimIndent()
            )
        ) {
            var rotationY by remember { mutableStateOf(20f) }

            data class LineData(val quadrant: Int, val startXFraction: Float, val endYFraction: Float)
            val lines = remember {
                (0..20).map {
                    LineData(
                        quadrant = Random.nextInt(4),
                        startXFraction = Random.nextFloat() * .25f,
                        endYFraction = Random.nextFloat() * .25f,
                    )
                }
            }

            val animatedRotation by animateFloatAsState(
                targetValue = rotationY,
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                )
            )

            Cube(
                modifier = Modifier
                    .size(300.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            rotationY += dragAmount * .2f
                        }
                    },
                angleX = -20f,
                angleY = animatedRotation,
            ) { face ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(color = Black.copy(alpha = .7f))
                            for (line in lines) {
                                rotate(degrees = 90f * line.quadrant) {
                                    drawLine(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Lime500, Purple500, Purple300)
                                        ),
                                        start = Offset(x = line.startXFraction * size.width, y = 0f),
                                        end = Offset(x = 0f, y = line.endYFraction * size.height),
                                        strokeWidth = 1.dp.toPx(),
                                    )
                                }
                            }
                        }
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Lime500, Purple500, Purple300)
                            ),
                            shape = RectangleShape,
                        )
                        .innerShadow(shape = RectangleShape) {
                            brush = Brush.linearGradient(
                                colors = listOf(Lime500, Purple500, Purple300)
                            )
                            radius = 120f
                            alpha = .2f
                        }
                )
            }
        }
    }
}
