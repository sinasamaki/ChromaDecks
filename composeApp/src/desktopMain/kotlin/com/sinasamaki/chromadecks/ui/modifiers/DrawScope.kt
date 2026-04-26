package com.sinasamaki.chromadecks.ui.modifiers

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer

fun DrawScope.layer(
    bounds: Rect = size.toRect(),
    block: DrawScope.() -> Unit
) =
    drawIntoCanvas { canvas ->
        canvas.withSaveLayer(
            bounds = bounds,
            paint = Paint(),
        ) { block() }
    }