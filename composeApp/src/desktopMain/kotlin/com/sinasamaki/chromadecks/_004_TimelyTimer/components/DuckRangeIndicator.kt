package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.Transparent
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A small standalone diagram, meant to sit beside (never on top of) [DuckingNumberDial], that
 * visualises the ducking [windowDegrees] as an abstract "cone": a curved base whose angular width
 * animates to match the active ducking range, capped at each end by a short line perpendicular to
 * the base — and, only once ducking stops being binary, a triangular point rising rightward out of
 * the middle of that base to show the push growing continuously toward the target.
 *
 * The base is one (mostly off-canvas) arc of a circle centred to the left of this composable, so
 * the middle of the range naturally bulges furthest to the right while both ends curve back toward
 * the dial — that curvature is what turns "two end-caps plus a raised middle point" into a cone
 * rather than a flat triangle, and it's also why the cone's point rises further to the right,
 * away from the dial, rather than back over it.
 *
 * @param symmetric When false, one edge stays anchored at 0° instead of the range being centred —
 *   used where the range represents a one-sided window (e.g. anchored at the 360° seam) rather
 *   than a duck centred on a target. Either way the arc spans the real window: ±[windowDegrees]
 *   when centred, [windowDegrees] in one direction when anchored.
 * @param oneSidedCaps When false (default), each end-cap's line radiates through its point in both
 *   directions, as usual. When true, it's trimmed to only the half pointing deeper into the cone's
 *   reach — used where the other half would otherwise shoot back out past the anchor instead of
 *   converging toward a point.
 * @param alpha Fades the whole diagram, applied to the colours it draws with rather than through a
 *   layer — this thing always overflows its own bounds, and `Modifier.alpha` would clip it.
 */
@Composable
fun DuckRangeIndicator(
    swatch: Swatch,
    duckMode: NumberDuckMode,
    modifier: Modifier = Modifier,
    windowDegrees: Float = 15f,
    symmetric: Boolean = true,
    oneSidedCaps: Boolean = false,
    alpha: Float = 1f,
) {
    val targetHalfWidthDeg = when (duckMode) {
        NumberDuckMode.NONE, NumberDuckMode.POP_EXACT -> 0f
        else -> windowDegrees
    }
    val targetConeHeight = if (duckMode == NumberDuckMode.SMOOTH) 1f else 0f

    val halfWidthDeg = remember { Animatable(targetHalfWidthDeg) }
    val coneHeight = remember { Animatable(targetConeHeight) }

    LaunchedEffect(duckMode) {
        launch {
            halfWidthDeg.animateTo(
                targetHalfWidthDeg,
                spring(
//                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),
            )
        }
        launch {
            coneHeight.animateTo(
                targetConeHeight,
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),
            )
        }
    }

    Canvas(modifier) {
        if (alpha <= 0f) return@Canvas

        val lineColor = swatch.v100.copy(alpha = alpha)
        val fillColor = swatch.v100.copy(alpha = .2f * alpha)

//        drawRect(
//            color = Red400.copy(alpha = .2f)
//        )
        val w = size.width
        val h = size.height

        val radius = h * .55f
        val anchorX = w * 0.22f
        val circleCenter = Offset( h * .5f, h / 2f)

        val visualScalePerDegree = 1f//2.2f
        val angleDeg = halfWidthDeg.value * visualScalePerDegree
        val angleRad = angleDeg * (PI.toFloat() / 180f)

        val startDeg = if (symmetric) -angleDeg else 0f
        val endDeg = if (symmetric) angleDeg else -angleDeg
        val startRad = startDeg * (PI.toFloat() / 180f)
        val endRad = endDeg * (PI.toFloat() / 180f)

        fun pointAtRad(theta: Float) = Offset(
            circleCenter.x + radius * cos(theta),
            circleCenter.y + radius * sin(theta),
        )

        fun radialAtRad(theta: Float) = Offset(cos(theta), sin(theta))

        val top = pointAtRad(startRad)
        val bottom = pointAtRad(endRad)
        val middle = pointAtRad((startRad + endRad) / 2f)

        val rect = Rect(center = circleCenter, radius = radius)

        val capHalfLength = 10.dp.toPx()
        fun drawCap(point: Offset, theta: Float) {
            val radial = radialAtRad(theta)
            val far = point + radial * (capHalfLength * 20f)
            val farSolid = point + radial * capHalfLength
            val gradientStart = if (oneSidedCaps) point else point - radial * (capHalfLength * 20f)
            val gradientColors = if (oneSidedCaps) {
                listOf(lineColor, Transparent)
            } else {
                listOf(Transparent, lineColor, Transparent)
            }
            drawLine(
                brush = Brush.linearGradient(colors = gradientColors, start = gradientStart, end = far),
                start = gradientStart,
                end = far,
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawLine(
                color = lineColor,
                start = if (oneSidedCaps) point else point - radial * capHalfLength,
                end = farSolid,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
        drawCap(top, startRad)
        drawCap(bottom, endRad)

        drawArc(
            brush = Brush.radialGradient(
                colors = listOf(
                    Transparent,
                    fillColor,
                )
            ),
            startAngle = startDeg,
            sweepAngle = endDeg - startDeg,
            useCenter = true,
            topLeft = rect.topLeft,
            size = rect.size,
        )

        drawArc(
            color = lineColor,
            startAngle = startDeg,
            sweepAngle = endDeg - startDeg,
            useCenter = false,
            topLeft = rect.topLeft,
            size = rect.size,
            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round),
        )

        val extraReach = (w - middle.x).coerceAtLeast(0f) * 0.9f
        val apex = middle - Offset(coneHeight.value * extraReach, 0f)

        val conePath = Path().apply {
            moveTo(top.x, top.y)
            lineTo(apex.x, apex.y)
            lineTo(bottom.x, bottom.y)
            arcTo(rect, endDeg, startDeg - endDeg, forceMoveTo = false)
            close()
        }
//        drawPath(
//            path = conePath,
//            color = swatch.v500.copy(alpha = 0.55f)
//        )
//        drawPath(
//            path = conePath,
//            color = swatch.v200.copy(alpha = 0.8f),
//            style = Stroke(
//                width = 1.dp.toPx()
//            )
//        )
    }
}
