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

internal const val HALF_TURN = 180f

internal const val QUARTER_TURN = 90f

internal const val START_ANGLE = -90f

fun Modifier.ribbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    progress: () -> Float,
    width: () -> Float = { 1f },
    sequential: Boolean = true,
    brush: ((index: Int, segment: RibbonSegment) -> Brush)? = null,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
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
                drawRibbonSegment(
                    segment = segment,
                    progress = progressValue,
                    widthPx = widthPx,
                    brush = brush?.invoke(index, segment) ?: segment.brush,
                    sequential = sequential,
                )
            }
        }
        drawContent()
        segments.forEachIndexed { index, segment ->
            if (segment.inFront) {
                drawRibbonSegment(
                    segment = segment,
                    progress = progressValue,
                    widthPx = widthPx,
                    brush = brush?.invoke(index, segment) ?: segment.brush,
                    sequential = sequential,
                )
            }
        }
    }
}

internal fun Modifier.flatRibbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    progress: () -> Float,
    brush: ((index: Int, segment: RibbonSegment) -> Brush)? = null,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    val segments = ribbonSegments(size, strokePx, loops - .5f, colors)

    onDrawWithContent {
        val progressValue = progress()
        drawContent()
        segments.forEachIndexed { index, segment ->
            drawRibbonSegment(segment, progressValue, strokePx, brush?.invoke(index, segment) ?: segment.brush)
        }
    }
}

internal fun Modifier.stagedRibbon(
    colors: List<Color>,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    content: Boolean,
    behindProgress: () -> Float,
    frontProgress: () -> Float,
): Modifier = drawWithCache {
    val strokePx = stroke.toPx()
    val segments = ribbonSegments(size, strokePx, loops - .5f, colors)

    onDrawWithContent {
        val behind = behindProgress()
        segments.forEach { if (!it.inFront) drawRibbonSegment(it, behind, strokePx) }
        if (content) drawContent()
        val front = frontProgress()
        segments.forEach { if (it.inFront) drawRibbonSegment(it, front, strokePx) }
    }
}

internal class RibbonCurve(
    val size: Size,
    val strokePx: Float,
    val loops: Float,
) {
    val start = Offset(0f, size.height * .5f)
    val end = Offset(size.width, size.height * .5f)

    val radius = (size.height * .5f) + strokePx

    val first = min(START_ANGLE, (360f * loops) - START_ANGLE)
    val last = max(START_ANGLE, (360f * loops) - START_ANGLE)
    val sweep = last - first

    val isEmpty = size.isEmpty() || sweep <= 0f || radius <= 0f

    fun centerAt(degrees: Float): Offset = lerp(start, end, (degrees - first) / sweep)

    fun pointAt(degrees: Float): Offset = ribbonPoint(degrees, radius, ::centerAt)

    val halfTurnBounds: List<Float> by lazy {
        val count = ceil(sweep / HALF_TURN).toInt()
        List(count + 1) { min(first + (it * HALF_TURN), last) }
    }

    val halfTurnCount: Int get() = halfTurnBounds.size - 1

    fun halfTurnPath(index: Int): Path = ribbonPath(
        fromDegrees = halfTurnBounds[index],
        toDegrees = halfTurnBounds[index + 1],
        radius = radius,
        centerAt = ::centerAt,
    )
}

internal fun ribbonHalfTurnPaths(size: Size, strokePx: Float, loops: Float): List<Path> {
    val curve = RibbonCurve(size, strokePx, loops)
    if (curve.isEmpty) return emptyList()
    return List(curve.halfTurnCount) { curve.halfTurnPath(it) }
}

internal fun ribbonSamples(
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

class RibbonSegment internal constructor(
    val path: Path,
    val length: Float,
    val from: Float,
    val to: Float,
    val startY: Float,
    val endY: Float,
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

        val startY = curve.pointAt(curve.halfTurnBounds[index]).y
        val endY = curve.pointAt(curve.halfTurnBounds[index + 1]).y

        RibbonSegment(
            path = path,
            length = lengths[index],
            from = from,
            to = to,
            startY = startY,
            endY = endY,
            brush = colors.verticalGradient(from = from, to = to, startY = startY, endY = endY),
            inFront = index % 2 == 1,
        )
    }
}

internal fun ribbonProgressAt(
    curve: RibbonCurve,
    segments: List<RibbonSegment>,
    degrees: Float,
): Float {
    if (curve.isEmpty || segments.isEmpty()) return 0f
    if (degrees >= curve.last) return 1f
    val clamped = degrees.coerceIn(curve.first, curve.last)
    val bounds = curve.halfTurnBounds
    val index = (0 until curve.halfTurnCount)
        .firstOrNull { clamped < bounds[it + 1] }
        ?: (curve.halfTurnCount - 1)
    val segment = segments[index]

    val partial = PathMeasure().apply {
        setPath(ribbonPath(bounds[index], clamped, curve.radius, curve::centerAt), false)
    }.length
    val local = if (segment.length > 0f) (partial / segment.length).coerceIn(0f, 1f) else 1f
    return lerp(segment.from, segment.to, local)
}

internal fun DrawScope.drawRibbonSegment(
    segment: RibbonSegment,
    progress: Float,
    widthPx: Float,
    brush: Brush = segment.brush,
    sequential: Boolean = true,
    tail: Float = 0f,
) {
    val slice = { value: Float ->
        if (sequential) {
            ((value - segment.from) / (segment.to - segment.from)).coerceIn(0f, 1f)
        } else {
            value.coerceIn(0f, 1f)
        }
    }
    val local = slice(progress)
    val localTail = slice(tail)
    if (local <= localTail || widthPx <= 0f) return

    val drawn = segment.length * (local - localTail)
    val skipped = segment.length * localTail
    drawPath(
        path = segment.path,
        brush = brush,
        style = Stroke(
            width = widthPx,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = when {
                local >= 1f && localTail <= 0f -> null
                localTail <= 0f -> PathEffect.dashPathEffect(floatArrayOf(drawn, segment.length))
                else -> PathEffect.dashPathEffect(
                    intervals = floatArrayOf(drawn, segment.length),
                    phase = drawn + segment.length - skipped,
                )
            },
        ),
    )
}

internal fun ribbonPath(
    fromDegrees: Float,
    toDegrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
    handleScale: Float = 1f,
): Path = Path().apply {
    val quarters = ceil((toDegrees - fromDegrees) / QUARTER_TURN).toInt().coerceAtLeast(1)
    val step = (toDegrees - fromDegrees) / quarters

    ribbonPoint(fromDegrees, radius, centerAt).let { moveTo(it.x, it.y) }
    repeat(quarters) { index ->
        val points = ribbonCubicPoints(
            fromDegrees = fromDegrees + (index * step),
            toDegrees = fromDegrees + ((index + 1) * step),
            radius = radius,
            centerAt = centerAt,
            handleScale = handleScale,
        )
        cubicTo(points[1].x, points[1].y, points[2].x, points[2].y, points[3].x, points[3].y)
    }
}

internal fun ribbonCubicPoints(
    fromDegrees: Float,
    toDegrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
    handleScale: Float = 1f,
): List<Offset> {
    val a0 = -fromDegrees.toRadians()
    val a1 = -toDegrees.toRadians()

    val handle = (4f / 3f) * tan((a1 - a0) / 4f) * radius
    val p0 = Offset(cos(a0), sin(a0)) * radius
    val p3 = Offset(cos(a1), sin(a1)) * radius
    val h0 = Offset(-sin(a0), cos(a0)) * handle
    val h3 = Offset(-sin(a1), cos(a1)) * handle

    val origin = { fraction: Float -> centerAt(lerp(fromDegrees, toDegrees, fraction)) }

    val anchor0 = p0 + origin(0f)
    val anchor3 = p3 + origin(1f)

    val handle0 = (p0 + h0 + origin(1f / 3f)) - anchor0
    val handle3 = (p3 - h3 + origin(2f / 3f)) - anchor3

    return listOf(
        anchor0,
        anchor0 + (handle0 * handleScale),
        anchor3 + (handle3 * handleScale),
        anchor3,
    )
}

internal fun ribbonPoint(
    degrees: Float,
    radius: Float,
    centerAt: (degrees: Float) -> Offset,
): Offset {
    val angle = -degrees.toRadians()
    return Offset(cos(angle), sin(angle)) * radius + centerAt(degrees)
}

internal fun List<Color>.verticalGradient(
    from: Float,
    to: Float,
    startY: Float,
    endY: Float,
): Brush {
    if (abs(endY - startY) < 1f) return SolidColor(sampleAt((from + to) * .5f))
    val stops = gradientStops(from, to)
    return Brush.verticalGradient(colorStops = stops.toTypedArray(), startY = startY, endY = endY)
}

internal fun List<Color>.gradientStops(from: Float, to: Float): List<Pair<Float, Color>> {
    val steps = size
    return buildList {
        add(0f to sampleAt(from))
        for (index in 1 until steps - 1) {
            val position = index / (steps - 1f)
            if (position > from && position < to) {
                add(((position - from) / (to - from)) to this@gradientStops[index])
            }
        }
        add(1f to sampleAt(to))
    }
}

internal fun List<Color>.sampleAt(fraction: Float): Color {
    if (size == 1) return first()
    val scaled = fraction.coerceIn(0f, 1f) * (size - 1)
    val index = floor(scaled).toInt().coerceIn(0, size - 2)
    return lerp(this[index], this[index + 1], scaled - index)
}

internal fun Float.toRadians(): Float = this * (PI / 180f).toFloat()
