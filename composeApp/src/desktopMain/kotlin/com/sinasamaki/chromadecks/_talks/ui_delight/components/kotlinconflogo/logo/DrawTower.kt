package com.sinasamaki.kotlinconf.logo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawTower(
    progress: Float = 0f
) {
    val towerRidgesProgress = progress * .25f
    val tower = Path().apply {

        addArc(
            oval = Rect(
                offset = Offset(39f, 0f),
                size = Size(2f, 2f)
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 360f,
        )

        moveTo(40, 2)
        lineTo(40, 4)

        addArc(
            oval = Rect(
                offset = Offset(31f, 4f),
                size = Size(18f, 14f)
            ),
            startAngleDegrees = 26f,
            sweepAngleDegrees = -180f - 52f,
        )

        moveTo(32, 14)
        lineTo(48, 14)

        moveTo(32, 14)
        lineTo(32, 19)
        lineTo(28, 23)
        lineTo(32, 23)
        lineTo(34, 21)
        lineTo(46, 21)
        lineTo(48, 23)
        lineTo(52, 23)
        lineTo(48, 19)
        lineTo(48, 14)

        moveTo(31, 23)
        lineTo(31, 35)

        moveTo(34, 21)
        lineTo(34, 35)

        moveTo(49, 23)
        lineTo(49, 35)

        moveTo(46, 21)
        lineTo(46, 35)


        moveTo(31, 35)
        lineTo(28, 39)
        lineTo(32, 39)
        lineTo(34, 37)
        lineTo(46, 37)
        lineTo(48, 39)
        lineTo(52, 39)
        lineTo(49, 35)
        lineTo(31, 35)

        moveTo(34, 37)
        lineTo(34, 49)

        moveTo(46, 37)
        lineTo(46, 49)

        moveTo(28, 49)
        lineTo(52, 49)

        moveTo(28, 53)
        lineTo(31, 53)
        lineTo(33, 51)
        lineTo(47, 51)
        lineTo(49, 53)
        lineTo(52, 53)

        moveTo(33, 51)
        lineTo(33, 64)

        moveTo(47, 51)
        lineTo(47, 64)

        moveTo(37, 35)
        lineTo(37, 28)
        quadraticTo(
            37f, 26f,
            40f, 24f
        )
        quadraticTo(
            43f, 26f,
            43f, 28f
        )
        lineTo(43, 35)

        addRect(
            rect = Rect(
                offset = Offset(39f, 51f),
                size = Size(2f, 2f)
            ),
        )

        addRect(
            rect = Rect(
                offset = Offset(39f, 57f),
                size = Size(2f, 2f)
            ),
        )
    }

    clipRect(
        left = 28f,
        right = 52f,
    ) {
        drawPath(
            path = tower,
            brush = brush,
            style = Stroke(
                width = strokeWidth,
            )
        )
    }

    for (i in 0..4) {
        val progress = ((towerRidgesProgress * 2) + (i / 4f)) % 1f
        val ridge = Path().apply {
            moveTo(40, 4)
            relativeCubicTo(
                lerp(.0001f, -.0001f, progress), 0f,
                lerp(11.6f, -11.6f, progress), 0f,
                lerp(8f, -8f, progress), 10f
            )
        }

        drawPath(
            path = ridge,
            brush = brush,
            style = Stroke(
                width = strokeWidth,
            )
        )
    }

    drawOval(
        brush = brush,
        style = Stroke(width = strokeWidth),
        topLeft = Offset(36f, 39f),
        size = Size(8f, 8f)
    )

    rotate(
        degrees = progress * 360f * 3,
        pivot = Offset(40f, 43f)
    ) {
        drawLine(
            brush = brush,
            start = Offset(40f, 43f),
            end = Offset(40f, 40f),
            strokeWidth = strokeWidth,
        )
    }

    rotate(
        degrees = progress * 360f,
        pivot = Offset(40f, 43f)
    ) {
        drawLine(
            brush = brush,
            start = Offset(40f, 43f),
            end = Offset(40f, 41f),
            strokeWidth = strokeWidth,
        )
    }

    drawCircle(
        brush = brush,
        center = Offset(40f, 43f),
        radius = .3f
    )

    drawLine(
        brush = brush,
        start = Offset(32f, 19f),
        end = Offset(48f, 19f),
        strokeWidth = strokeWidth,
    )

    for (i in 0..3) {
        val windowPath = Path().apply {
            moveTo(34.6f + (i * 3.6f), 19f)
            relativeLineTo(0f, -2.8f)
        }.let { path ->
            expandPathCubic(
                source = path,
                padding = 0.5f,
                cornerRadius = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                )
            )
        }

        drawPath(
            path = windowPath,
            brush = brush,
            style = Stroke(
                width = strokeWidth,
            )
        )
    }

    for (i in 0..1) {
        val windowPath = Path().apply {
            moveTo(38.5f + (i * 3f), 35f)
            relativeLineTo(0f, -6.4f)
        }.let { path ->
            expandPathCubic(
                source = path,
                padding = 1.5f,
                cornerRadius = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                )
            )
        }

        drawPath(
            path = windowPath,
            brush = brush,
            style = Stroke(
                width = strokeWidth,
            )
        )
    }


}

@Preview
@Composable
private fun DrawTowerPreview() {
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
            drawTower(progress = progress.value)
        }
    }
}
