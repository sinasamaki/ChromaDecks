package com.sinasamaki.kotlinconf.logo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import com.sinasamaki.kotlinconf.utils.lineTo
import com.sinasamaki.kotlinconf.utils.moveTo
import androidx.compose.ui.tooling.preview.Preview

fun DrawScope.drawBoundary() {
    val boundary = Path().apply {
        moveTo(0, 0)
        lineTo(0, 128)
        lineTo(128, 128)
        lineTo(64, 64)
        lineTo(128, 0)
        close()

        moveTo(0, 64)
        lineTo(64, 64)

        moveTo(28, 0)
        lineTo(28, 128)

        moveTo(52, 0)
        lineTo(52, 64)

        moveTo(73, 0)
        lineTo(73, 55)

        moveTo(97, 0)
        lineTo(97, 31)

        moveTo(0, 106)
        lineTo(106, 106)

        moveTo(28, 85)
        lineTo(85, 85)

        moveTo(52, 39)
        lineTo(73, 39)

        moveTo(52, 29)
        lineTo(73, 29)

        moveTo(0, 44)
        lineTo(28, 44)

        moveTo(8, 44)
        lineTo(8, 106)

        moveTo(0, 85)
        lineTo(8, 85)

        moveTo(0, 23)
        lineTo(28, 23)

        moveTo(21, 23)
        lineTo(21, 44)


        moveTo(97, 10)
        lineTo(118, 10)

        moveTo(64, 64)
        lineTo(64, 106)

        moveTo(112, 112)
        lineTo(112, 128)

        moveTo(53, 85)
        lineTo(53, 106)

        moveTo(43, 85)
        lineTo(53, 96)
        lineTo(43, 106)
    }

    drawPath(
        path = boundary,
        brush = brush,
        style = Stroke(
            width = strokeWidth,
        )
    )
}

@Preview
@Composable
private fun DrawBoundaryPreview() {
    Canvas(
        modifier = Modifier
            .background(Color.Black)
            .size(200.dp)
    ) {
        scale(scale = size.width / 128f, pivot = Offset.Zero) {
            drawBoundary()
        }
    }
}
