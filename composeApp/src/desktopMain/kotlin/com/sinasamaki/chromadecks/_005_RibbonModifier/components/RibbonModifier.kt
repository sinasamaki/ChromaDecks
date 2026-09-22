package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.tan

/** A half turn of the helix — the chunk that is either in front of the content, or behind it. */
internal const val HALF_TURN = 180f

/** The largest arc a single cubic can approximate without visible error. */
internal const val QUARTER_TURN = 90f

/** The sweep starts and ends at the bottom of the loop, so both tips tuck under the content. */
internal const val START_ANGLE = -90f

/**
 * Wraps a ribbon around the content: a helix drawn as one path per half turn, the odd ones
 * on top of the content and the even ones underneath.
 *
 * [colors] is walked along the length of the whole ribbon, so each half turn gets a vertical
 * gradient that picks up exactly where the previous one left off.
 *
 * @param progress how much of the ribbon is drawn, 0..1 across every turn.
 * @param width multiplier on [stroke]. A lambda, like [progress], so animating either one
 *   redraws without rebuilding the geometry.
 */
fun Modifier.ribbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    progress: () -> Float,
    width: () -> Float = { 1f },
    brush: ((index: Int) -> Brush)? = null,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    // drawWithCache rebuilds this only when the size or these parameters change; the lambdas
    // above keep it alive while the ribbon animates.
    val segments = ribbonSegments(
        size = size,
        strokePx = strokePx,
        loops = loops - .5f,
        colors = colors,
    )

    onDrawWithContent {
        val progressValue = progress()
        val widthPx = strokePx * width()
        segments.forEachIndexed { index, segment ->
            if (!segment.inFront) {
                drawRibbonSegment(segment, progressValue, widthPx, brush?.invoke(index) ?: segment.brush)
            }
        }
        drawContent()
        segments.forEachIndexed { index, segment ->
            if (segment.inFront) {
                drawRibbonSegment(segment, progressValue, widthPx, brush?.invoke(index) ?: segment.brush)
            }
        }
    }
}

/**
 * The same ribbon drawn in one pass, entirely on top of the content — the stage the deck shows
 * before splitting the path up, where the effect still reads as a spiral laid over the button
 * rather than wound around it.
 */
internal fun Modifier.flatRibbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    progress: () -> Float,
    brush: ((index: Int) -> Brush)? = null,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    val segments = ribbonSegments(size, strokePx, loops - .5f, colors)

    onDrawWithContent {
        val progressValue = progress()
        drawContent()
        segments.forEachIndexed { index, segment ->
            drawRibbonSegment(segment, progressValue, strokePx, brush?.invoke(index) ?: segment.brush)
        }
    }
}

/**
 * The layering taken one pass at a time, so a slide can land on the same three lines the code
 * does: the half turns behind, then the content, then the half turns in front.
 *
 * @param stage 0 draws only what goes behind, 1 adds the content, 2 adds what comes in front.
 */
internal fun Modifier.stagedRibbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    stage: Int,
    progress: () -> Float,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    val segments = ribbonSegments(size, strokePx, loops - .5f, colors)

    onDrawWithContent {
        val progressValue = progress()
        segments.forEach { if (!it.inFront) drawRibbonSegment(it, progressValue, strokePx) }
        if (stage >= 1) drawContent()
        if (stage >= 2) {
            segments.forEach { if (it.inFront) drawRibbonSegment(it, progressValue, strokePx) }
        }
    }
}

/**
 * The helix the ribbon is drawn along: a circle of [radius] whose center slides from the left
 * edge of [size] to the right edge while the angle sweeps [loops] times around.
 *
 * Everything on a slide — the modifier, the construction diagram, the exploded view — measures
 * itself against this one object, so none of them can drift apart.
 */
internal class RibbonCurve(
    val size: Size,
    val strokePx: Float,
    /** Whole turns wanted, already halved down by the caller (see [Modifier.ribbon]). */
    val loops: Float,
) {
    val start = Offset(0f, size.height * .5f)
    val end = Offset(size.width, size.height * .5f)

    /** Big enough to clear the top and bottom edges of whatever is being wrapped. */
    val radius = (size.height * .5f) + strokePx

    val first = min(START_ANGLE, (360f * loops) - START_ANGLE)
    val last = max(START_ANGLE, (360f * loops) - START_ANGLE)
    val sweep = last - first

    val isEmpty = size.isEmpty() || sweep <= 0f || radius <= 0f

    /** Where the circle's center sits when the sweep has reached [degrees]. */
    fun centerAt(degrees: Float): Offset = lerp(start, end, (degrees - first) / sweep)

    fun pointAt(degrees: Float): Offset = helixPoint(degrees, radius, ::centerAt)

    /** The angles the path is cut at: every half turn, where it crosses the top and bottom. */
    val halfTurnBounds: List<Float> by lazy {
        val count = ceil(sweep / HALF_TURN).toInt()
        List(count + 1) { min(first + (it * HALF_TURN), last) }
    }

    val halfTurnCount: Int get() = halfTurnBounds.size - 1

    fun halfTurnPath(index: Int): Path = helixPath(
        fromDegrees = halfTurnBounds[index],
        toDegrees = halfTurnBounds[index + 1],
        radius = radius,
        centerAt = ::centerAt,
    )
}

/** One path per half turn, in draw order — even indices go behind the content, odd in front. */
internal fun ribbonHalfTurnPaths(size: Size, strokePx: Float, loops: Float): List<Path> {
    val curve = RibbonCurve(size, strokePx, loops)
    if (curve.isEmpty) return emptyList()
    return List(curve.halfTurnCount) { curve.halfTurnPath(it) }
}

/** Points along the helix every [step] degrees, for the construction diagram. */
internal fun helixSamples(
    size: Size,
    strokePx: Float,
    loops: Float,
    step: Float = QUARTER_TURN,
): List<Offset> {
    val curve = RibbonCurve(size, strokePx, loops)
    if (curve.isEmpty || step <= 0f) return emptyList()
    val count = floor(curve.sweep / step).toInt()
    return List(count + 1) { curve.pointAt(curve.first + (it * step)) }
}

internal class RibbonSegment(
    val path: Path,
    val length: Float,
    /** Where this segment starts and ends along the whole ribbon, 0..1. */
    val from: Float,
    val to: Float,
    val brush: Brush,
    val inFront: Boolean,
)

internal fun ribbonSegments(
    size: Size,
    strokePx: Float,
    loops: Float,
    colors: List<Color>,
): List<RibbonSegment> {
    val curve = RibbonCurve(size, strokePx, loops)
    if (curve.isEmpty || colors.isEmpty()) return emptyList()

    val paths = List(curve.halfTurnCount) { curve.halfTurnPath(it) }

    val measure = PathMeasure()
    val lengths = paths.map { measure.setPath(it, false); measure.length }
    val total = lengths.sum()
    if (total <= 0f) return emptyList()

    var travelled = 0f
    return paths.mapIndexed { index, path ->
        val from = travelled / total
        travelled += lengths[index]
        val to = travelled / total

        // Vertical gradient running the way this segment runs, so the colour it ends on is the
        // colour its neighbour starts from.
        val startY = curve.pointAt(curve.halfTurnBounds[index]).y
        val endY = curve.pointAt(curve.halfTurnBounds[index + 1]).y

        RibbonSegment(
            path = path,
            length = lengths[index],
            from = from,
            to = to,
            brush = colors.verticalGradient(from = from, to = to, startY = startY, endY = endY),
            inFront = index % 2 == 1,
        )
    }
}

internal fun DrawScope.drawRibbonSegment(
    segment: RibbonSegment,
    progress: Float,
    widthPx: Float,
    brush: Brush = segment.brush,
) {
    // Each segment owns a slice of the overall progress, and they are laid out end to end by
    // length — so the reveal reads as one continuous stroke crossing segment boundaries.
    val local = ((progress - segment.from) / (segment.to - segment.from)).coerceIn(0f, 1f)
    if (local <= 0f || widthPx <= 0f) return

    drawPath(
        path = segment.path,
        brush = brush,
        style = Stroke(
            width = widthPx,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = if (local >= 1f) null else PathEffect.dashPathEffect(
                // One dash on, then a gap long enough to swallow the rest of the segment.
                intervals = floatArrayOf(segment.length * local, segment.length),
            ),
        ),
    )
}

/** Builds one stretch of the helix out of quarter-turn cubics. */
internal fun helixPath(
    fromDegrees: Float,
    toDegrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
    handleScale: Float = 1f,
): Path = Path().apply {
    val quarters = ceil((toDegrees - fromDegrees) / QUARTER_TURN).toInt().coerceAtLeast(1)
    val step = (toDegrees - fromDegrees) / quarters

    helixPoint(fromDegrees, radius, centerAt).let { moveTo(it.x, it.y) }
    repeat(quarters) { index ->
        val points = helixCubicPoints(
            fromDegrees = fromDegrees + (index * step),
            toDegrees = fromDegrees + ((index + 1) * step),
            radius = radius,
            centerAt = centerAt,
            handleScale = handleScale,
        )
        cubicTo(points[1].x, points[1].y, points[2].x, points[2].y, points[3].x, points[3].y)
    }
}

/**
 * The four control points of the cubic that approximates one quarter turn: start, two handles,
 * end. Exposed so the teaching diagram can draw the handles it is built from.
 */
internal fun helixCubicPoints(
    fromDegrees: Float,
    toDegrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
    /** 0 collapses the handles onto the anchors, leaving straight lines and hard corners. */
    handleScale: Float = 1f,
): List<Offset> {
    val a0 = -fromDegrees.toRadians()
    val a1 = -toDegrees.toRadians()

    // The classic circular-arc approximation: handles of 4/3·tan(sweep/4) leave the arc within
    // a fraction of a pixel of a real circle for sweeps this size.
    val handle = (4f / 3f) * tan((a1 - a0) / 4f) * radius
    val p0 = Offset(cos(a0), sin(a0)) * radius
    val p3 = Offset(cos(a1), sin(a1)) * radius
    val h0 = Offset(-sin(a0), cos(a0)) * handle
    val h3 = Offset(-sin(a1), cos(a1)) * handle

    // A cubic reproduces a straight line exactly when its controls sample that line at
    // 0, ⅓, ⅔ and 1 — so the travelling origin rides along for free, no extra segments needed.
    val origin = { fraction: Float -> centerAt(lerp(fromDegrees, toDegrees, fraction)) }

    val anchor0 = p0 + origin(0f)
    val anchor3 = p3 + origin(1f)

    // Each handle is scaled along its own offset from the anchor, which is the curve's tangent
    // there — the circle's tangent plus the third of the drift the control point carries. So a
    // shrinking handle slides down the tangent onto the anchor instead of drifting off it, and
    // a scale of 0 leaves a straight line between anchors.
    val handle0 = (p0 + h0 + origin(1f / 3f)) - anchor0
    val handle3 = (p3 - h3 + origin(2f / 3f)) - anchor3

    return listOf(
        anchor0,
        anchor0 + (handle0 * handleScale),
        anchor3 + (handle3 * handleScale),
        anchor3,
    )
}

internal fun helixPoint(
    degrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
): Offset {
    val angle = -degrees.toRadians()
    return Offset(cos(angle), sin(angle)) * radius + centerAt(degrees)
}

/**
 * The slice of [this] between [from] and [to], as a gradient from [startY] to [endY]. Colours
 * from the list that fall inside the slice become stops of their own, so a longer segment steps
 * through them instead of skipping to the end.
 */
internal fun List<Color>.verticalGradient(
    from: Float,
    to: Float,
    startY: Float,
    endY: Float,
): Brush {
    if (abs(endY - startY) < 1f) return SolidColor(sampleAt((from + to) * .5f))

    val steps = size
    val stops = buildList<Pair<Float, Color>> {
        add(0f to sampleAt(from))
        for (index in 1 until steps - 1) {
            val position = index / (steps - 1f)
            if (position > from && position < to) {
                add(((position - from) / (to - from)) to this@verticalGradient[index])
            }
        }
        add(1f to sampleAt(to))
    }
    return Brush.verticalGradient(colorStops = stops.toTypedArray(), startY = startY, endY = endY)
}

internal fun List<Color>.sampleAt(fraction: Float): Color {
    if (size == 1) return first()
    val scaled = fraction.coerceIn(0f, 1f) * (size - 1)
    val index = floor(scaled).toInt().coerceIn(0, size - 2)
    return lerp(this[index], this[index + 1], scaled - index)
}

internal fun Float.toRadians(): Float = this * (PI / 180f).toFloat()
