package com.sinasamaki.chromadecks._002_PathAnimations

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill


/**
    To simplify all the path diagrams, I will be using
    a 100x100 grid. This function scales the path relative
    to its container before rendering it
 **/
fun DrawScope.drawScaledPath(
    path: Path,
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DefaultBlendMode
) {
    val scaleMatrix = Matrix()
    scaleMatrix.scale(x = size.width / 100f, y = size.height / 100f)
    path.copy().let { path ->
        path.transform(scaleMatrix)
        drawPath(
            path = path,
            color = color,
            alpha = alpha,
            style = style,
            colorFilter = colorFilter,
            blendMode = blendMode
        )
    }
}

fun DrawScope.drawScaledPath(
    path: Path,
    brush: Brush,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DefaultBlendMode
) {
    val scaleMatrix = Matrix()
    scaleMatrix.scale(x = size.width / 100f, y = size.height / 100f)
    path.copy().let { path ->
        path.transform(scaleMatrix)
        drawPath(
            path = path,
            brush = brush,
            alpha = alpha,
            style = style,
            colorFilter = colorFilter,
            blendMode = blendMode
        )
    }
}