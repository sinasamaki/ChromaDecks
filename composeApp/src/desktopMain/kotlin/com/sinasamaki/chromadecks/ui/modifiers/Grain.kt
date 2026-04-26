package com.sinasamaki.chromadecks.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.grain_dark
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun Modifier.grain(
    bitmapAlpha: Float = .5f,
    bitmapScale: Float = 1f,
    drawAlpha: Float = .7f,
    blendMode: BlendMode = BlendMode.Overlay
): Modifier {
    val painter = painterResource(Res.drawable.grain_dark)

    return drawWithCache {
        val grainSize = 512f * bitmapScale
        val bitmap = painter.toImageBitmap(
            size = Size(grainSize, grainSize),
            density = Density(density),
            layoutDirection = layoutDirection,
            alpha = bitmapAlpha
        )

        onDrawWithContent {
            drawContent()
            clipRect {
                for (i in 0..((size.width / grainSize).roundToInt())) {
                    for (j in 0..((size.height / grainSize).roundToInt())) {
                        val topLeft = Offset(
                            x = (i * grainSize).toFloat(),
                            y = (j * grainSize).toFloat()
                        )
                        val pivot = topLeft + Offset(grainSize / 2, grainSize / 2)
                        rotate(
                            degrees = 90f * Random.nextInt(9),
                            pivot = pivot
                        ) {
                            scale(
                                scaleX = if (Random.nextBoolean()) 1f else -1f,
                                scaleY = if (Random.nextBoolean()) 1f else -1f,
                                pivot = pivot,
                            ) {
                                drawImage(
                                    image = bitmap,
                                    topLeft = topLeft,
                                    alpha = drawAlpha,
                                    blendMode = blendMode,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

fun Painter.toImageBitmap(
    size: Size,
    density: Density,
    layoutDirection: LayoutDirection,
    alpha: Float,
): ImageBitmap {
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)
    CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
        draw(size, alpha = alpha)
    }
    return bitmap
}