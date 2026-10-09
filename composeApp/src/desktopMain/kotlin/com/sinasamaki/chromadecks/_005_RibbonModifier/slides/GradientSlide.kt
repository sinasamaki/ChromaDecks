package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._005_RibbonModifier.RIBBON_COLORS
import com.sinasamaki.chromadecks._005_RibbonModifier.components.CodePanel
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonCurve
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonSegment
import com.sinasamaki.chromadecks._005_RibbonModifier.components.gradientStops
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbonSegments
import com.sinasamaki.chromadecks._005_RibbonModifier.components.sampleAt
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc900

private const val STAGE_SOLID = 0
private const val STAGE_HORIZONTAL = 1
private const val STAGE_POINTS = 2
private const val STAGE_SEGMENTS = 3
private const val STAGE_RAMP = 4
internal const val STAGE_ALONG_PATH = 5

private const val LOOPS = 4

private const val SEGMENT_COUNT = LOOPS * 2

private const val POINT_COUNT = SEGMENT_COUNT + 1

private val STROKE = 20.dp

private val SEGMENT_START = Zinc700
private val SEGMENT_END = Zinc300

private const val GUIDE_END = .45f

private const val BOW = .22f
private const val INJECT_END = 1.3f
private const val FADE_END = 1.8f

internal const val INJECTION_END = (POINT_COUNT - 1) + FADE_END

internal data class GradientState(
    val stage: Int,
    val code: String,
)

internal class GradientSlide : ListSlideAdvanced<GradientState>() {

    override val initialState: GradientState
        get() = GradientState(stage = STAGE_SOLID, code = SOLID_CODE)

    override val stateMutations: List<GradientState.() -> GradientState>
        get() = listOf(
            { copy(stage = STAGE_HORIZONTAL, code = HORIZONTAL_CODE) },
            { copy(stage = STAGE_POINTS, code = POINTS_CODE) },
            { copy(stage = STAGE_SEGMENTS, code = SEGMENTS_CODE) },
            { copy(stage = STAGE_RAMP, code = RAMP_CODE) },
            { copy(stage = STAGE_ALONG_PATH, code = ALONG_PATH_CODE) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: GradientState) {
        val progress by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800),
            label = "gradient-progress",
        )

        val points by animateFloatAsState(
            targetValue = if (state.stage >= STAGE_POINTS) POINT_COUNT.toFloat() else 0f,
            animationSpec = tween(durationMillis = POINT_COUNT * 140, easing = LinearEasing),
            label = "gradient-points",
        )

        val ramp by animateFloatAsState(
            targetValue = if (state.stage >= STAGE_RAMP) 1f else 0f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            label = "gradient-ramp",
        )

        val injected by animateFloatAsState(
            targetValue = if (state.stage >= STAGE_ALONG_PATH) INJECTION_END else 0f,
            animationSpec = tween(durationMillis = (INJECTION_END * 520).toInt(), easing = LinearEasing),
            label = "gradient-injected",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                GradientRibbon(
                    stage = state.stage,
                    progress = { progress },
                    points = { points },
                    ramp = { ramp },
                    injected = { injected },
                )
            }

            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.1f),
            )
        }
    }
}

@Composable
internal fun GradientRibbon(
    stage: Int,
    progress: () -> Float,
    points: () -> Float,
    ramp: () -> Float,
    injected: () -> Float,
    modifier: Modifier = Modifier,
) {
    val brush: ((Int, RibbonSegment) -> Brush)? = when {
        stage == STAGE_SOLID -> { _, _ -> SolidColor(Rose500) }
        stage < STAGE_SEGMENTS -> { _, _ -> Brush.horizontalGradient(RIBBON_COLORS) }
        else -> { index, segment ->
            segment.blendedBrush(
                colors = RIBBON_COLORS,
                startBlend = dose(injected() - index),
                endBlend = dose(injected() - (index + 1)),
            )
        }
    }

    HabitRow(
        modifier = modifier
            .width(440.dp)
            .height(124.dp)
            .colorInjection(
                colors = RIBBON_COLORS,
                points = points,
                ramp = ramp,
                injected = injected,
            )
            .ribbon(
                colors = RIBBON_COLORS,
                stroke = STROKE,
                loops = LOOPS,
                progress = progress,
                brush = brush,
            ),
    )
}

private fun phase(t: Float, start: Float, end: Float): Float =
    FastOutSlowInEasing.transform(((t - start) / (end - start)).coerceIn(0f, 1f))

private fun dose(t: Float): Float = phase(t, GUIDE_END, INJECT_END)

private fun Modifier.colorInjection(
    colors: List<Color>,
    points: () -> Float,
    ramp: () -> Float,
    injected: () -> Float,
): Modifier = drawWithCache {
    val strokePx = STROKE.toPx()
    val curve = RibbonCurve(size, strokePx, LOOPS - .5f)
    val segments = ribbonSegments(size, strokePx, LOOPS - .5f, colors)

    val rampTop = size.height + (strokePx * 1.5f) + 64.dp.toPx()
    val rampHeight = 14.dp.toPx()
    val rampBrush = Brush.horizontalGradient(colors, startX = 0f, endX = size.width)

    val injections = List(minOf(POINT_COUNT, curve.halfTurnBounds.size)) { index ->
        val fraction = segments.getOrNull(index)?.from ?: segments.last().to
        val start = Offset(size.width * fraction, rampTop + (rampHeight / 2f))
        val end = curve.pointAt(curve.halfTurnBounds[index])

        val chord = end - start
        val normal = Offset(-chord.y, chord.x) / chord.getDistance().coerceAtLeast(1f)
        val outwards = if (start.x < size.width / 2f) -1f else 1f
        val sideways = if (normal.x * outwards >= 0f) normal else -normal
        val control = start + (chord / 2f) + (sideways * chord.getDistance() * BOW)

        val path = Path().apply {
            moveTo(start.x, start.y)
            quadraticTo(control.x, control.y, end.x, end.y)
        }
        Injection(
            path = path,
            length = PathMeasure().apply { setPath(path, false) }.length,
            start = start,
            end = end,
            color = colors.sampleAt(fraction),
        )
    }

    onDrawWithContent {
        val injectedValue = injected()

        drawContent()

        val shown = ramp()
        if (shown > 0f) {
            clipRect(right = size.width * shown, bottom = size.height * 4f) {
                drawRoundRect(
                    brush = rampBrush,
                    topLeft = Offset(0f, rampTop),
                    size = Size(size.width, rampHeight),
                    cornerRadius = CornerRadius(rampHeight / 2f),
                )
            }
        }

        injections.forEachIndexed { index, injection ->
            val t = injectedValue - index
            drawInjectionLines(injection, t)
            if (t > 0f && t < FADE_END) {
                val fade = 1f - phase(t, INJECT_END, FADE_END)
                drawCircle(White, radius = 9.dp.toPx(), center = injection.start, alpha = fade)
                drawCircle(injection.color, radius = 6.dp.toPx(), center = injection.start, alpha = fade)
            }
        }

        val pointsValue = points()
        injections.forEachIndexed { index, injection ->
            val appear = phase(pointsValue - index, 0f, 1f)
            val visible = appear * (1f - dose(injectedValue - index))
            if (visible <= 0f) return@forEachIndexed
            drawCircle(White, radius = 10.dp.toPx() * appear, center = injection.end, alpha = visible)
            drawCircle(Zinc900, radius = 6.dp.toPx() * appear, center = injection.end, alpha = visible)
        }
    }
}

private fun DrawScope.drawInjectionLines(injection: Injection, t: Float) {
    if (t <= 0f || t >= FADE_END) return
    val fade = 1f - phase(t, INJECT_END, FADE_END)
    drawPath(
        path = injection.path,
        color = Zinc400,
        alpha = fade,
        style = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = injection.dash(phase(t, 0f, GUIDE_END)),
        ),
    )
    val dose = dose(t)
    if (dose > 0f) {
        drawPath(
            path = injection.path,
            color = injection.color,
            alpha = fade,
            style = Stroke(
                width = 6.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = injection.dash(dose),
            ),
        )
    }
}

private class Injection(
    val path: Path,
    val length: Float,
    val start: Offset,
    val end: Offset,
    val color: Color,
) {
    fun dash(amount: Float): PathEffect =
        PathEffect.dashPathEffect(floatArrayOf(length * amount, length + 1f))
}

private fun RibbonSegment.blendedBrush(colors: List<Color>, startBlend: Float, endBlend: Float): Brush {
    val stops = colors.gradientStops(from, to).map { (position, color) ->
        val blend = lerp(startBlend, endBlend, position)
        position to lerp(lerp(SEGMENT_START, SEGMENT_END, position), color, blend)
    }
    return Brush.verticalGradient(colorStops = stops.toTypedArray(), startY = startY, endY = endY)
}

private val SOLID_CODE = """
drawPath(
    path = segment.path,
    color = Rose500,
)
""".trimIndent()

private val HORIZONTAL_CODE = """
drawPath(
    path = segment.path,
    brush = Brush.horizontalGradient(colors),
)
""".trimIndent()

private val POINTS_CODE = """
val segments = ribbonSegments(size)

segments.forEach { segment ->
    drawPoint(segment.start)
    drawPoint(segment.end)
}
""".trimIndent()

private val SEGMENTS_CODE = """
segments.forEach { segment ->
    val brush = Brush.verticalGradient(
        0f to DarkGray,
        1f to LightGray,
        startY = segment.startY,
        endY = segment.endY,
    )
    drawPath(segment.path, brush)
}
""".trimIndent()

private val RAMP_CODE = """
fun List<Color>.sampleAt(fraction: Float): Color {
    val scaled = fraction * lastIndex
    val index = floor(scaled).toInt()
        .coerceAtMost(lastIndex - 1)

    return lerp(
        this[index],
        this[index + 1],
        scaled - index,
    )
}
""".trimIndent()

private val ALONG_PATH_CODE = """
val from = colors.sampleAt(segment.from)
val to = colors.sampleAt(segment.to)

val brush = Brush.verticalGradient(
    0f to from,
    1f to to,
    startY = segment.startY,
    endY = segment.endY,
)
""".trimIndent()
