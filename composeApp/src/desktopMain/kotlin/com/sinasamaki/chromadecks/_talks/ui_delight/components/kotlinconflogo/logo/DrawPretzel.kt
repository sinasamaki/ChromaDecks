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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import com.sinasamaki.kotlinconf.utils.oscillateToZero
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawPretzel(progress: Float) {
    val path = Path().apply {
        moveTo(26, 125)
        lineTo(14, 110)
        cubicTo(
            8f, 104f,
            2f, 112f,
            2f, 115f,
        )
        cubicTo(
            0f, 126f,
            14f, 126f,
            14f, 126f,
        )

    }.let { path ->
        expandPathCubic(
            source = path,
            padding = 1.5f,
        )
    }

    val left = path.copy()
    left.transform(
        matrix = Matrix().apply {
            scale(x = -1f)
        }
    )
    left.transform(
        matrix = Matrix().apply {
            translate(x = 28f)
        }
    )

    clipRect(
        left = 0f,
        right = 28f,
    ) {
        rotate(
            degrees = lerp(-4f, 4f, progress.oscillateToZero(5)),
            pivot = Offset(15f, 117f)
        ) {
            translate(
                top = lerp(-.25f, .5f, progress.oscillateToZero(2))
            ) {
                drawPath(
                    path = path.or(left),
                    brush = brush,
                    style = Stroke(
                        width = strokeWidth,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun DrawPretzelPreview() {
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
            drawPretzel(progress = progress.value)
        }
    }
}
