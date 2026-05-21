package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.growingSemiCircles(
    progress: Float,
    bounds: Rect,
    vertical: Boolean,
) {
    val progress = (progress * 4f) % 1f
    clipRect(
        left = bounds.left,
        top = bounds.top,
        right = bounds.right,
        bottom = bounds.bottom,
    ) {
        rotate(
            degrees = if (vertical) 0f else -90f,
            pivot = bounds.center,
        ) {
            for (i in 0..2) {
                val y = (-(bounds.center.y - bounds.top) * i) + bounds.height / 2 * progress
                val halfH = bounds.height / 2f
                val scale = when {
                    y < -bounds.height -> .8f
                    y < -halfH -> lerp(.8f, 0f, (y + bounds.height) / halfH)
                    y < 0f -> 0f
                    else -> lerp(0f, .8f, y / halfH)
                }.coerceIn(0f, 1f).let {
                    FastOutSlowInEasing.transform(it)
                }
                translate(
                    top = y
                ) {
                    drawLine(
                        brush = brush,
                        strokeWidth = strokeWidth,
                        start = Offset(bounds.left, bounds.center.y),
                        end = Offset(bounds.right, bounds.center.y)
                    )
                    drawPath(
                        path = Path().apply {
                            addArc(
                                oval = bounds.copy(
                                    left = bounds.left + bounds.width / 2 * scale,
                                    bottom = bounds.bottom - bounds.height / 2 * scale,
                                    right = bounds.right - bounds.width / 2 * scale,
                                    top = bounds.top + bounds.height / 2 * scale,
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = 180f
                            )
                        },
                        brush = brush,
                        style = Stroke(strokeWidth),
                    )
                }
            }
        }
    }
    drawRect(
        brush = brush,
        topLeft = bounds.topLeft,
        size = bounds.size,
        style = Stroke(strokeWidth)
    )
}

@Preview
@Composable
private fun GrowingSemiCirclesVerticalPreview() {
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
            .size(200.dp)
    ) {
        scale(scale = size.width / 128f, pivot = Offset.Zero) {
            growingSemiCircles(
                progress = progress.value,
                bounds = Rect(
                    offset = Offset(32f, 32f),
                    size = Size(64f, 64f),
                ),
                vertical = true,
            )
        }
    }
}

@Preview
@Composable
private fun GrowingSemiCirclesHorizontalPreview() {
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
            .size(200.dp)
    ) {
        scale(scale = size.width / 128f, pivot = Offset.Zero) {
            growingSemiCircles(
                progress = progress.value,
                bounds = Rect(
                    offset = Offset(32f, 32f),
                    size = Size(64f, 64f),
                ),
                vertical = false,
            )
        }
    }
}
