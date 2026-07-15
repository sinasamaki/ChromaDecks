package com.sinasamaki.chromadecks._004_TimelyTimer.components

/*
 * Compose port of christiandeange/NumberView (https://github.com/christiandeange/NumberView),
 * itself a port of the Timely app number-morph animation.
 *
 * Original library: Copyright 2017 Christian De Angelis, licensed under the Apache License 2.0.
 * Only the digit Bézier control-point data is reused here; the rendering is rewritten from
 * scratch for Jetpack Compose (no Android View interop). CHANGES: ported from Android Canvas/View
 * (Java) to a Compose DrawScope, restructured the digit data, wrote new tween/layout composables.
 *
 *
 */

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The design space every glyph is authored in (140 x 200), from the original library.
private const val DESIGN_W = 140f
private const val DESIGN_H = 200f

/**
 * A single glyph: 5 anchor [points] joined by 4 cubic Bézier segments with control points
 * ([c1], [c2]). Every glyph shares these counts so any two can tween point-for-point. [minX]/[maxX]
 * are the ink bounds (over anchors and controls), giving each digit its own tight advance width.
 */
private class Glyph(
    val points: List<Offset>,   // size 5 — moveTo(points[0]) then cubicTo(..points[i+1])
    val c1: List<Offset>,       // size 4
    val c2: List<Offset>,       // size 4
) {
    val minX: Float = (points + c1 + c2).minOf { it.x }
    val maxX: Float = (points + c1 + c2).maxOf { it.x }
}

private fun p(x: Float, y: Float) = Offset(x, y)

// Digit shapes 0..9 (index == digit). Coordinates lifted verbatim from the original library.
private val DIGITS: List<Glyph> = listOf(
    // 0
    Glyph(
        points = listOf(p(14.5f, 100f), p(70f, 18f), p(126f, 100f), p(70f, 180f), p(14.5f, 100f)),
        c1 = listOf(p(14.5f, 60f), p(103f, 18f), p(126f, 140f), p(37f, 180f)),
        c2 = listOf(p(37f, 18f), p(126f, 60f), p(103f, 180f), p(14.5f, 140f)),
    ),
    // 1
    Glyph(
        points = listOf(p(15f, 20.5f), p(42.5f, 20.5f), p(42.5f, 181f), p(42.5f, 181f), p(42.5f, 181f)),
        c1 = listOf(p(15f, 20.5f), p(42.5f, 20.5f), p(42.5f, 181f), p(42.5f, 181f)),
        c2 = listOf(p(15f, 20.5f), p(42.5f, 20.5f), p(42.5f, 181f), p(42.5f, 181f)),
    ),
    // 2
    Glyph(
        points = listOf(p(26f, 60f), p(114.5f, 61f), p(78f, 122f), p(27f, 177f), p(117f, 177f)),
        c1 = listOf(p(29f, 2f), p(114.5f, 78f), p(64f, 138f), p(27f, 177f)),
        c2 = listOf(p(113f, 4f), p(100f, 98f), p(44f, 155f), p(117f, 177f)),
    ),
    // 3
    Glyph(
        points = listOf(p(33.25f, 54f), p(69.5f, 18f), p(69.5f, 96f), p(70f, 180f), p(26.5f, 143f)),
        c1 = listOf(p(33f, 27f), p(126f, 18f), p(128f, 96f), p(24f, 180f)),
        c2 = listOf(p(56f, 18f), p(116f, 96f), p(120f, 180f), p(26f, 150f)),
    ),
    // 4
    Glyph(
        points = listOf(p(125f, 146f), p(13f, 146f), p(99f, 25f), p(99f, 146f), p(99f, 179f)),
        c1 = listOf(p(125f, 146f), p(13f, 146f), p(99f, 25f), p(99f, 146f)),
        c2 = listOf(p(13f, 146f), p(99f, 25f), p(99f, 146f), p(99f, 179f)),
    ),
    // 5
    Glyph(
        points = listOf(p(116f, 20f), p(61f, 20f), p(42f, 78f), p(115f, 129f), p(15f, 154f)),
        c1 = listOf(p(61f, 20f), p(42f, 78f), p(67f, 66f), p(110f, 183f)),
        c2 = listOf(p(61f, 20f), p(42f, 78f), p(115f, 85f), p(38f, 198f)),
    ),
    // 6
    Glyph(
        points = listOf(p(80f, 20f), p(80f, 20f), p(16f, 126f), p(123f, 126f), p(23f, 100f)),
        c1 = listOf(p(80f, 20f), p(41f, 79f), p(22f, 208f), p(116f, 66f)),
        c2 = listOf(p(80f, 20f), p(18f, 92f), p(128f, 192f), p(46f, 64f)),
    ),
    // 7
    Glyph(
        points = listOf(p(17f, 21f), p(128f, 21f), p(90.67f, 73.34f), p(53.34f, 126.67f), p(16f, 181f)),
        c1 = listOf(p(17f, 21f), p(128f, 21f), p(90.67f, 73.34f), p(53.34f, 126.67f)),
        c2 = listOf(p(128f, 21f), p(90.67f, 73.34f), p(53.34f, 126.67f), p(16f, 181f)),
    ),
    // 8
    Glyph(
        points = listOf(p(71f, 96f), p(71f, 19f), p(71f, 96f), p(71f, 179f), p(71f, 96f)),
        c1 = listOf(p(14f, 95f), p(124f, 19f), p(14f, 96f), p(124f, 179f)),
        c2 = listOf(p(14f, 19f), p(124f, 96f), p(6f, 179f), p(124f, 96f)),
    ),
    // 9
    Glyph(
        points = listOf(p(117f, 100f), p(17f, 74f), p(124f, 74f), p(60f, 180f), p(60f, 180f)),
        c1 = listOf(p(94f, 136f), p(12f, 8f), p(122f, 108f), p(60f, 180f)),
        c2 = listOf(p(24f, 134f), p(118f, -8f), p(99f, 121f), p(60f, 180f)),
    ),
)

/**
 * Draws the glyph tweened from [from] to [to] by [t] (0..1) as a rounded stroke. Scale is fixed by
 * height; horizontally the glyph is shifted so its interpolated left ink edge [minX] sits [padPx]
 * from the left. Every point/control is linearly interpolated.
 */
private fun DrawScope.drawGlyph(
    from: Glyph,
    to: Glyph,
    t: Float,
    color: Color,
    stroke: Float,
    minX: Float,
    padPx: Float,
) {
    val scale = size.height / DESIGN_H

    fun blend(a: Offset, b: Offset): Offset = Offset(
        ((a.x + (b.x - a.x) * t) - minX) * scale + padPx,
        (a.y + (b.y - a.y) * t) * scale,
    )

    val path = Path()
    val start = blend(from.points[0], to.points[0])
    path.moveTo(start.x, start.y)
    for (i in 0 until 4) {
        val cc1 = blend(from.c1[i], to.c1[i])
        val cc2 = blend(from.c2[i], to.c2[i])
        val end = blend(from.points[i + 1], to.points[i + 1])
        path.cubicTo(cc1.x, cc1.y, cc2.x, cc2.y, end.x, end.y)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}

/**
 * A single digit (0..9), [height] tall, that morphs whenever [digit] changes. Its width hugs the
 * glyph and animates over the same [durationMillis] as the morph, so "1" is narrow and "8" is wide.
 */
@Composable
fun TimelyNumber(
    digit: Int,
    height: Dp,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Dp = 5.dp,
    durationMillis: Int = 700,
) {
    val target = digit.coerceIn(0, 9)
    var from by remember { mutableStateOf(target) }
    var to by remember { mutableStateOf(target) }
    val factor = remember { Animatable(1f) }

    LaunchedEffect(target) {
        if (target != to) {
            from = to
            to = target
            factor.snapTo(0f)
            factor.animateTo(1f, tween(durationMillis, easing = FastOutSlowInEasing))
        }
    }

    val fromG = DIGITS[from]
    val toG = DIGITS[to]
    val t = factor.value
    // Interpolate the ink bounds so the width animates with the morph.
    val minX = fromG.minX + (toG.minX - fromG.minX) * t
    val maxX = fromG.maxX + (toG.maxX - fromG.maxX) * t

    // Side padding so the rounded stroke isn't clipped at the edges.
    val pad = strokeWidth * 0.6f
    val width = (height / DESIGN_H) * (maxX - minX) + pad * 2f

    Canvas(modifier.width(width).height(height)) {
        drawGlyph(fromG, toG, t, color, strokeWidth.toPx(), minX, pad.toPx())
    }
}

/**
 * A two-part "LL:RR" readout (four [TimelyNumber]s and a colon), each digit morphing independently.
 * Unit-agnostic — pass minutes:seconds, hours:minutes, etc. [fontSize] maps to the glyph height. A
 * custom [Layout] pins the colon to the centre while the (morphing-width) digits hug inward.
 */
@Composable
fun TimelyTime(
    left: Int,
    right: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 48.sp,
    strokeWidth: Dp = 5.dp,
    spacing: Dp = 2.dp,
) {
    val digitHeight = with(LocalDensity.current) { fontSize.toDp() }
    val l = left.coerceIn(0, 99)
    val r = right.coerceIn(0, 99)

    Layout(
        modifier = modifier,
        content = {
            TimelyNumber(l / 10, digitHeight, color = color, strokeWidth = strokeWidth)
            TimelyNumber(l % 10, digitHeight, color = color, strokeWidth = strokeWidth)
            Colon(color, digitHeight, strokeWidth)
            TimelyNumber(r / 10, digitHeight, color = color, strokeWidth = strokeWidth)
            TimelyNumber(r % 10, digitHeight, color = color, strokeWidth = strokeWidth)
        },
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val (leftTens, leftOnes, colon, rightTens, rightOnes) = measurables.map { it.measure(loose) }

        val gap = spacing.roundToPx()

        // Equal half-width each side so the centred colon also sits at the parent's centre.
        val leftExtent = leftTens.width + leftOnes.width + gap * 2
        val rightExtent = rightTens.width + rightOnes.width + gap * 2
        val half = maxOf(leftExtent, rightExtent)
        val totalWidth = half * 2 + colon.width
        val totalHeight = maxOf(leftTens.height, colon.height, rightTens.height)

        layout(totalWidth, totalHeight) {
            fun yOf(h: Int) = (totalHeight - h) / 2
            colon.place(half, yOf(colon.height))
            val leftOnesX = half - gap - leftOnes.width
            leftOnes.place(leftOnesX, yOf(leftOnes.height))
            leftTens.place(leftOnesX - gap - leftTens.width, yOf(leftTens.height))
            val rightTensX = half + colon.width + gap
            rightTens.place(rightTensX, yOf(rightTens.height))
            rightOnes.place(rightTensX + rightTens.width + gap, yOf(rightOnes.height))
        }
    }
}

@Composable
private fun Colon(color: Color, height: Dp, strokeWidth: Dp) {
    Canvas(Modifier.size(height * 0.34f, height)) {
        val r = strokeWidth.toPx() * 0.9f
        val cx = size.width / 2f
        drawCircle(color, r, Offset(cx, size.height * 0.4f))
        drawCircle(color, r, Offset(cx, size.height * 0.6f))
    }
}
