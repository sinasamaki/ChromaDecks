package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawParallelLines(
    vertical: Boolean,
    topLeft: Offset,
    size: Size,
    clip: Path = Path().apply {
        addRect(
            rect = Rect(topLeft, size)
        )
    },
    progress: Float = 1f,
) {

    drawPath(
        path = clip,
        brush = brush,
        style = Stroke(width = strokeWidth)
    )
    clipPath(
        path = clip
    ) {
        translate(
            left = topLeft.x,
            top = topLeft.y,
        ) {
            for (i in 0..10) {
                if (vertical) {
                    val length = size.width + (4 - (size.width % 4))
                    val x = (4f * i + progress * length) % length
                    drawLine(
                        brush = brush,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth,
                    )
                } else {
                    val length = size.height + (4 - (size.height % 4))
                    val y = (4f * i + progress * length) % length
                    drawLine(
                        brush = brush,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DrawParallelLinesTrianglePreview() {
    val progress = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(durationMillis = 4000, easing = LinearEasing))
        }
    }
    Canvas(
        modifier = Modifier
            .background(Color.Black)
            .size(100.dp)
    ) {
        scale(scale = size.width / 128f, pivot = Offset.Zero) {
            drawParallelLines(
                vertical = true,
                topLeft = Offset(20f, 20f),
                size = Size(88f, 88f),
                clip = Path().apply {
                    moveTo(64f, 20f)
                    lineTo(108f, 108f)
                    lineTo(20f, 108f)
                    close()
                },
                progress = progress.value,
            )
        }
    }
}

@Preview
@Composable
private fun DrawParallelLinesTrapezoidPreview() {
    val progress = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(durationMillis = 4000, easing = LinearEasing))
        }
    }
    Canvas(
        modifier = Modifier
            .background(Color.Black)
            .size(100.dp)
    ) {
        scale(scale = size.width / 128f, pivot = Offset.Zero) {
            drawParallelLines(
                vertical = false,
                topLeft = Offset(20f, 30f),
                size = Size(88f, 68f),
                clip = Path().apply {
                    moveTo(44f, 30f)
                    lineTo(84f, 30f)
                    lineTo(108f, 98f)
                    lineTo(20f, 98f)
                    close()
                },
                progress = progress.value,
            )
        }
    }
}
