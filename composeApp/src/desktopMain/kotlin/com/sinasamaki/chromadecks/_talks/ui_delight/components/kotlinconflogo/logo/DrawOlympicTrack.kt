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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.kotlinconf.utils.expandPathCubic
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawOlympicTrack(progress: Float = 1f) {

    clipRect(
        left = 8f,
        top = 64f,
        right = 28f,
        bottom = 106f,
    ) {
        for (i in 0..3) {
            val progress = ((progress * 1f) + i / 3f) % 1f
            val path = Path().apply {
                moveTo(18, 74)
                lineTo(18, 96)
            }.let { path ->
                expandPathCubic(
                    source = path,
                    padding = lerp(2f, 13.5f, progress)
                )

            }

            drawPath(
                path = path,
                brush = brush,
                style = Stroke(
                    width = strokeWidth * (progress / .2f).coerceAtMost(1f),
                )
            )
        }
    }

}

@Preview
@Composable
private fun DrawOlympicTrackPreview() {
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
            drawOlympicTrack(progress = progress.value)
        }
    }
}
