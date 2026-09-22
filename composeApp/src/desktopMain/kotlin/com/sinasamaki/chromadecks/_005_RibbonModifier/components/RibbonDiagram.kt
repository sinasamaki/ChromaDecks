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

/**
 * The construction diagram: a point on a circle, a circle whose center slides along a line, and
 * the helix that falls out of doing both at once.
 *
 * Every shape here is drawn with the same helpers the modifier itself uses, so what the slide
 * teaches and what the ribbon does cannot drift apart.
 *
 * @param sweep how far the angle has travelled from the start, in degrees.
 * @param centerTravel 0 pins the circle in place, 1 slides it the full width as the sweep runs.
 * @param centerOverride parks the center at this fraction of the line instead of deriving it from
 *   the sweep — for showing the circle sliding on its own, with no ribbon forming behind it.
 * @param radiusScale shrinks the circle, so the unit circle can grow into a radius.
 * @param handleScale 0 leaves the path hard-cornered between its sample points, 1 curves it.
 */
@Composable
fun RibbonDiagram(
    modifier: Modifier = Modifier,
    sweep: Float,
    loops: Float = 1f,
    centerTravel: Float = 0f,
    centerOverride: Float? = null,
    radiusScale: Float = 1f,
    handleScale: Float = 1f,
    showCircle: Boolean = true,
    showRadius: Boolean = true,
    showTrail: Boolean = true,
    showSamples: Boolean = false,
    showHandles: Boolean = false,
    /** Ends the path at the last point dropped, rather than trailing the point around the circle. */
    trailToSamples: Boolean = false,
    accent: Color = Rose500,
    guide: Color = Zinc400,
    ink: Color = Zinc900,
    stroke: Dp = 8.dp,
) {
    Canvas(modifier = modifier) {
        val strokePx = stroke.toPx()
        // Sized so the loops stay spaced out as the count climbs; packed any tighter and the
        // helix stops reading as a spring and starts reading as stacked circles.
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

        // A parked center still needs the full line under it; only a circle with nowhere to go
        // sits in the middle of the diagram.
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
            visibility = travelling,
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

        // The path is only ever as long as the points it has been given, so it never reaches
        // ahead to the point still travelling round the circle.
        val pathEnd = if (trailToSamples) {
            first + (floor((current - first) / QUARTER_TURN) * QUARTER_TURN)
        } else {
            current
        }

        if (showTrail && pathEnd - first > .5f) {
            drawPath(
                path = helixPath(first, pathEnd, radius, centerAt, handleScale),
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

        val point = helixPoint(current, radius, centerAt)

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

/** The line the circle's center rides along, and how far along it the center has come. */
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

/** A small arc at the circle's center, showing how far the angle has come. */
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
        // Screen angles run the other way to the sweep, hence the negatives.
        startAngle = -first,
        sweepAngle = -min(travelled, 360f),
        useCenter = false,
        topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
        size = Size(arcRadius * 2f, arcRadius * 2f),
        style = Stroke(width = 2.dp.toPx()),
    )
}

/** One dot every quarter turn, appearing as the sweep reaches it. */
private fun DrawScope.drawSamples(
    first: Float,
    current: Float,
    radius: Float,
    centerAt: (Float) -> Offset,
    ink: Color,
) {
    val steps = floor((current - first) / QUARTER_TURN).toInt()
    for (index in 0..steps) {
        val point = helixPoint(first + (index * QUARTER_TURN), radius, centerAt)
        drawCircle(color = White, radius = 9.dp.toPx(), center = point)
        drawCircle(color = ink, radius = 5.dp.toPx(), center = point)
    }
}

/** The two control handles of every quarter-turn cubic, drawn the way a vector editor would. */
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
        val points = helixCubicPoints(
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
            drawCircle(color = White, radius = 7.dp.toPx(), center = handle)
            drawCircle(
                color = ink.copy(alpha = .55f),
                radius = 7.dp.toPx(),
                center = handle,
                style = Stroke(width = 2.dp.toPx()),
            )
        }
    }
}
