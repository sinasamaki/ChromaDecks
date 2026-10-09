package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp

internal fun Modifier.leadRibbon(
    colors: List<Color>,
    stroke: Dp,
    loops: Int,
    leadIn: () -> Float,
    leadOut: () -> Float,
    progress: () -> Float,
    tail: () -> Float = { 0f },
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    val curve = RibbonCurve(size, strokePx, loops - .5f)
    val wrap = ribbonSegments(size, strokePx, loops - .5f, colors)
    val wrapLength = wrap.sumOf { it.length.toDouble() }.toFloat()
    val start = curve.pointAt(curve.first)
    val end = curve.pointAt(curve.last)
    val first = colors.first()
    val last = colors.last()

    onDrawWithContent {
        if (wrap.isEmpty()) {
            drawContent()
            return@onDrawWithContent
        }

        val inLength = (leadIn() + strokePx).coerceAtLeast(0f)
        val outLength = (leadOut() + strokePx).coerceAtLeast(0f)
        val total = inLength + wrapLength + outLength
        val head = progress() * total
        val trail = tail().coerceAtMost(progress()) * total

        val inFrom = trail.coerceIn(0f, inLength)
        val inTo = head.coerceIn(0f, inLength)
        val wrapHead = ((head - inLength) / wrapLength).coerceIn(0f, 1f)
        val wrapTail = ((trail - inLength) / wrapLength).coerceIn(0f, 1f)
        val outFrom = (trail - inLength - wrapLength).coerceIn(0f, outLength)
        val outTo = (head - inLength - wrapLength).coerceIn(0f, outLength)

        if (inTo > inFrom) {
            drawLine(
                color = first,
                start = Offset(start.x - inLength + inFrom, start.y),
                end = Offset(start.x - inLength + inTo, start.y),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
        }
        wrap.forEach {
            if (!it.inFront) drawRibbonSegment(it, wrapHead, strokePx, tail = wrapTail)
        }
        drawContent()
        wrap.forEach {
            if (it.inFront) drawRibbonSegment(it, wrapHead, strokePx, tail = wrapTail)
        }
        if (outTo > outFrom) {
            drawLine(
                color = last,
                start = Offset(end.x + outFrom, end.y),
                end = Offset(end.x + outTo, end.y),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
        }
    }
}
