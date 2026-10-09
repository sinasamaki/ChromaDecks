package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlin.math.floor
import kotlin.math.min

@Composable
fun RibbonDiagram(
    modifier: Modifier = Modifier,
    sweep: Float,
    loops: Float = 1f,
    centerTravel: Float = 0f,
    centerOverride: Float? = null,
    travelVisibility: Float? = null,
    radiusScale: Float = 1f,
    handleScale: Float = 1f,
    showCircle: Boolean = true,
    showRadius: Boolean = true,
    showTrail: Boolean = true,
    showSamples: Boolean = false,
    showHandles: Boolean = false,
    trailToSamples: Boolean = false,
    accent: Color = Rose500,
    guide: Color = Zinc400,
    ink: Color = Zinc900,
    stroke: Dp = 8.dp,
) {
    Canvas(modifier = modifier) {
        val strokePx = stroke.toPx()
        val radius = (size.height * .3f)
            .coerceAtMost(size.width / ((1.8f * loops) + 2f))
            .coerceAtLeast(size.height * .13f) * radiusScale

        val room = (size.height * .3f) + strokePx
        val travelStart = Offset(room, size.height * .5f)
        val travelEnd = Offset(size.width - room, size.height * .5f)

        val first = START_ANGLE
        val total = 360f * loops
        val travelled = sweep.coerceIn(0f, total)
        val current = first + travelled

        val travelling = if (centerOverride != null) 1f else centerTravel
        val idle = Offset(((travelStart.x + travelEnd.x) * .5f) - travelStart.x, 0f) *
            (1f - travelling)
        val lineStart = travelStart + idle
        val lineEnd = travelEnd + idle

        val centerAt = { degrees: Float ->
            lerp(
                start = lineStart,
                stop = lineEnd,
                fraction = centerOverride ?: (((degrees - first) / total) * centerTravel),
            )
        }
        val center = centerAt(current)

        drawTravelLine(
            start = lineStart,
            end = lineEnd,
            guide = guide,
            travelled = centerOverride ?: (((current - first) / total) * centerTravel),
            visibility = travelVisibility ?: travelling,
        )

        if (showCircle) {
            drawCircle(
                color = guide,
                radius = radius,
                center = center,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 14f)),
                ),
            )
        }

        val pathEnd = if (trailToSamples) {
            first + (floor((current - first) / QUARTER_TURN) * QUARTER_TURN)
        } else {
            current
        }

        if (showTrail && pathEnd - first > .5f) {
            drawPath(
                path = ribbonPath(first, pathEnd, radius, centerAt, handleScale),
                color = accent,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }

        if (showHandles) {
            drawHandles(first, current, radius, centerAt, handleScale, ink)
        }

        if (showSamples) {
            drawSamples(first, current, radius, centerAt, ink)
        }

        val point = ribbonPoint(current, radius, centerAt)

        if (showRadius) {
            drawLine(
                color = ink.copy(alpha = .45f),
                start = center,
                end = point,
                strokeWidth = 2.dp.toPx(),
            )
            drawAngleArc(center, radius, first, travelled, ink)
            drawCircle(color = ink.copy(alpha = .45f), radius = 5.dp.toPx(), center = center)
        }

        drawCircle(color = White, radius = strokePx * .95f, center = point)
        drawCircle(color = accent, radius = strokePx * .62f, center = point)
    }
}

private fun DrawScope.drawTravelLine(
    start: Offset,
    end: Offset,
    guide: Color,
    travelled: Float,
    visibility: Float,
) {
    if (visibility <= 0f) return
    drawLine(
        color = guide.copy(alpha = guide.alpha * visibility),
        start = start,
        end = end,
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 14f)),
    )
    if (travelled > 0f) {
        drawLine(
            color = guide.copy(alpha = guide.alpha * visibility),
            start = start,
            end = lerp(start, end, travelled.coerceIn(0f, 1f)),
            strokeWidth = 3.dp.toPx(),
        )
    }
}

private fun DrawScope.drawAngleArc(
    center: Offset,
    radius: Float,
    first: Float,
    travelled: Float,
    ink: Color,
) {
    if (travelled <= 1f) return
    val arcRadius = radius * .28f
    drawArc(
        color = ink.copy(alpha = .45f),
        startAngle = -first,
        sweepAngle = -min(travelled, 360f),
        useCenter = false,
        topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
        size = Size(arcRadius * 2f, arcRadius * 2f),
        style = Stroke(width = 2.dp.toPx()),
    )
}

private fun DrawScope.drawSamples(
    first: Float,
    current: Float,
    radius: Float,
    centerAt: (Float) -> Offset,
    ink: Color,
) {
    val steps = floor((current - first) / QUARTER_TURN).toInt()
    for (index in 0..steps) {
        val point = ribbonPoint(first + (index * QUARTER_TURN), radius, centerAt)
        drawCircle(color = White, radius = 9.dp.toPx(), center = point)
        drawCircle(color = ink, radius = 5.dp.toPx(), center = point)
    }
}

private fun DrawScope.drawHandles(
    first: Float,
    current: Float,
    radius: Float,
    centerAt: (Float) -> Offset,
    handleScale: Float,
    ink: Color,
) {
    val steps = floor((current - first) / QUARTER_TURN).toInt()
    for (index in 0 until steps) {
        val points = ribbonCubicPoints(
            fromDegrees = first + (index * QUARTER_TURN),
            toDegrees = first + ((index + 1) * QUARTER_TURN),
            radius = radius,
            centerAt = centerAt,
            handleScale = handleScale,
        )
        listOf(points[0] to points[1], points[3] to points[2]).forEach { (anchor, handle) ->
            drawLine(
                color = ink.copy(alpha = .35f),
                start = anchor,
                end = handle,
                strokeWidth = 2.dp.toPx(),
            )
            drawCircle(color = White, radius = 3.5.dp.toPx(), center = handle)
            drawCircle(
                color = ink.copy(alpha = .55f),
                radius = 3.5.dp.toPx(),
                center = handle,
                style = Stroke(width = 1.5.dp.toPx()),
            )
        }
    }
}
