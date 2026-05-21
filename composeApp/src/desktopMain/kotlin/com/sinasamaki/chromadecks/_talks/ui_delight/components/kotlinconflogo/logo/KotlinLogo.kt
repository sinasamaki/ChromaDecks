package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import com.sinasamaki.kotlinconf.utils.oscillateToZero
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KotlinLogo(modifier: Modifier = Modifier) {

    val cells = 128
    var componentSize by remember { mutableStateOf(IntSize.Zero) }
    var selectedCell by remember { mutableStateOf(Pair(0, 0)) }

    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            progress.animateTo(
                1f,
                animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
            )
//            progress.animateTo(0f, animationSpec = tween(durationMillis = 2000, easing = LinearEasing))
            progress.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellX = (offset.x / (componentSize.width.toFloat() / cells)).toInt()
                        .coerceIn(0, cells - 1)
                    val cellY = (offset.y / (componentSize.height.toFloat() / cells)).toInt()
                        .coerceIn(0, cells - 1)
                    selectedCell = Pair(cellX, cellY)
                }
            }
            .drawBehind {
                componentSize = IntSize(size.width.toInt(), size.height.toInt())
                val smoothProgress = FastOutSlowInEasing.transform(progress.value)


                scale(
                    scale = size.width / cells,
                    pivot = Offset.Zero,
                ) {

                    drawBoundary()

                    clipPath(
                        path = Path().apply {
                            moveTo(0, 0)
                            lineTo(0, 128)
                            lineTo(128, 128)
                            lineTo(64, 64)
                            lineTo(128, 0)
                            lineTo(0, 0)
                        }
                    ) {

                        drawTower(progress.value)

                        translate(
                            left = 45f
                        ) {
                            scale(
                                scaleX = -1f,
                                scaleY = 1f,
                                pivot = Offset(40f, 0f)
                            ) {
                                drawTower(progress.value)
                            }
                        }

                        opposingCircles(progress.value.oscillateToZero(3))
                        drawHeart(progress.value)
                        drawOlympicTrack(progress.value)
                        drawParallelLines(
                            vertical = true,
                            topLeft = Offset(97f, 0f),
                            size = Size(32f, 10f),
                            progress = progress.value
                        )

                        drawParallelLines(
                            vertical = false,
                            topLeft = Offset(8f, 44f),
                            size = Size(20f, 20f),
                            clip = Path().apply {
                                moveTo(8, 44)
                                lineTo(8, 52)
                                lineTo(18, 64)
                                lineTo(28, 52)
                                lineTo(28, 44)
                            },
                            progress = -progress.value
                        )

                        clipRect(
                            left = 52f
                        ) {
                            drawParallelLines(
                                vertical = true,
                                topLeft = Offset(52f, 10f),
                                size = Size(21f, 19f),
                                clip = Path().apply {
                                    moveTo(52, 10)
                                    lineTo(73, 29)
                                    lineTo(73, 10)
                                    close()
                                },
                                progress = -progress.value
                            )
                        }

                        drawParallelLines(
                            vertical = true,
                            topLeft = Offset(64f, 64f),
                            size = Size(24f, 21f),
                            clip = Path().apply {
                                moveTo(64, 64)
                                lineTo(64, 85)
                                lineTo(85, 85)
                            },
                            progress = progress.value
                        )

                        drawParallelLines(
                            vertical = false,
                            topLeft = Offset(53f, 85f),
                            size = Size(11f, 21f),
                            progress = progress.value
                        )

                        drawParallelLines(
                            vertical = false,
                            topLeft = Offset(112f, 112f),
                            size = Size(16f, 21f),
                            progress = -progress.value
                        )


                        drawPretzel(progress.value)
                        drawMascot(progress.value)
                        bottomCapsules(progress.value)

                        growingSemiCircles(
                            progress = progress.value,
                            bounds = Rect(
                                offset = Offset(0f, 23f),
                                size = Size(21f, 21f)
                            ),
                            vertical = true,
                        )
                        growingSemiCircles(
                            progress = progress.value,
                            bounds = Rect(
                                offset = Offset(91f, 106f),
                                size = Size(21f, 22f)
                            ),
                            vertical = false,
                        )
                    }
                }
            }
    ) {
    }
}

@Preview
@Composable
private fun KotlinLogoPreview() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .size(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        KotlinLogo(modifier = Modifier.size(300.dp))
    }
}
