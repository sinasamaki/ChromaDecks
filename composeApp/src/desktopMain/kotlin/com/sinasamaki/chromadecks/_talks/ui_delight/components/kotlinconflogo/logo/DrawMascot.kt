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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawMascot(
    progress: Float = 1f,
) {
    val circle = Path().apply {
        addOval(
            oval = Rect(
                offset = Offset(71f, 85f),
                size = Size(21f, 21f)
            )
        )
    }

    val corner = Path().apply {
        moveTo(86f, 85f)
        lineTo(95.5f, 96f)
        lineTo(88.75f, 103f)
    }

    drawPath(
        path = circle.or(corner),
        brush = brush,
        style = Stroke(width = strokeWidth)
    )


    if (((progress * 37).toInt() % 17) != 0) {
        drawPath(
            path = Path().apply {
                addOval(
                    oval = Rect(
                        offset = Offset(83f, 93f),
                        size = Size(5f, 5f)
                    )
                )
            },
            brush = brush,
            style = Stroke(width = strokeWidth)
        )
    } else {
        drawLine(
            brush = brush,
            start = Offset(83f, 95.5f),
            end = Offset(88f, 95.5f),
            strokeWidth = 1f,
            cap = StrokeCap.Round
        )
    }

}

@Preview
@Composable
private fun DrawMascotPreview() {
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
            drawMascot(progress = progress.value)
        }
    }
}
