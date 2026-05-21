package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.oscillateToZero
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.bottomCapsules(
    progress: Float = 0f
) {
    val progress = 1f - progress.oscillateToZero(3)
    val delta = progress * 28f
    val left = expandPathCubic(
        source = Path().apply {
            moveTo(28f, 117f)
            relativeLineTo(delta, 0f)
        },
        padding = 11f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0),
        )
    )

    val right = expandPathCubic(
        source = Path().apply {
            moveTo(50f + delta, 117f)
            lineTo(80f, 117f)
        },
        padding = 11f,
        cornerRadius = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomStart = CornerSize(50),
            bottomEnd = CornerSize(50),
        )
    )

    drawPath(
        path = left,
        brush = brush,
        style = Stroke(
            width = strokeWidth
        )
    )

    drawPath(
        path = right,
        brush = brush,
        style = Stroke(
            width = strokeWidth
        )
    )

    clipPath(
        path = left,
    ) {
        translate(
            delta
        ) {
            for (i in 0..2) {
                val x = 28f + 4 * i
                drawLine(
                    brush = brush,
                    start = Offset(x, 102f),
                    end = Offset(x, 128f),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomCapsulesPreview() {
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
            bottomCapsules(progress = progress.value)
        }
    }
}
