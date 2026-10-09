package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import com.sinasamaki.chromadecks.ui.components.ExplodedView
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc900

@Composable
fun RibbonExploded(
    rotationX: Float,
    spacing: Dp,
    width: Dp,
    height: Dp,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    stroke: Dp = 20.dp,
    loops: Int = 3,
    progress: Float = 1f,
    trace: Float? = null,
    construction: Float = 0f,
    points: Float = 0f,
    widthScale: Float = 1f,
    sandwiched: Boolean = true,
    tint: Float = 0f,
    planes: Float = 0f,
    planeColor: Color = Zinc400,
    planeStroke: Dp = 2.dp,
    planeCorner: Dp = 16.dp,
    behindColor: Color = Indigo500,
    frontColor: Color = Rose500,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    val curve = remember(width, height, stroke, loops, density) {
        with(density) {
            RibbonCurve(
                size = Size(width.toPx(), height.toPx()),
                strokePx = stroke.toPx(),
                loops = loops - .5f,
            )
        }
    }
    val segments = remember(curve, colors) {
        ribbonSegments(curve.size, curve.strokePx, curve.loops, colors)
    }

    val traceDegrees = trace?.let { curve.first + (curve.sweep * it.coerceIn(0f, 1f)) }
    val drawnProgress = traceDegrees?.let { ribbonProgressAt(curve, segments, it) } ?: progress

    val overhang = (height / 2) + stroke
    val planeWidth = width + (overhang * 2)
    val planeHeight = height + (overhang * 2)

    val behindIndices = if (sandwiched) segments.indices.filter { !segments[it].inFront } else emptyList()
    val frontIndices = if (sandwiched) segments.indices.filter { segments[it].inFront } else segments.indices.toList()
    val behind = behindIndices.map { segments[it] }
    val front = frontIndices.map { segments[it] }

    ExplodedView(
        rotationX = rotationX,
        rotationY = rotationY,
        rotationZ = rotationZ,
        spacing = spacing,
        modifier = modifier,
        decorator = { layer ->
            Box(
                modifier = Modifier
                    .width(planeWidth)
                    .height(planeHeight)
                    .border(
                        width = planeStroke,
                        color = planeColor.copy(alpha = planeColor.alpha * planes),
                        shape = RoundedCornerShape(planeCorner),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                layer()
            }
        },
    ) {
        layer {
            SegmentPlane(behind, planeWidth, planeHeight, overhang, stroke * widthScale, drawnProgress, tint, behindColor) {
                if (points > 0f) behindIndices.forEach { drawCubicPoints(curve, it, points) }
            }
        }

        layer {
            Box(
                modifier = Modifier.size(planeWidth, planeHeight),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.size(width, height)) { content() }
            }
        }

        layer {
            SegmentPlane(front, planeWidth, planeHeight, overhang, stroke * widthScale, drawnProgress, tint, frontColor) {
                if (points > 0f) frontIndices.forEach { drawCubicPoints(curve, it, points) }
                if (construction > 0f) {
                    drawConstruction(curve, traceDegrees ?: curve.first, construction)
                }
            }
        }
    }
}

@Composable
private fun SegmentPlane(
    segments: List<RibbonSegment>,
    planeWidth: Dp,
    planeHeight: Dp,
    overhang: Dp,
    stroke: Dp,
    progress: Float,
    tint: Float,
    groupColor: Color,
    overlay: DrawScope.() -> Unit = {},
) {
    Canvas(modifier = Modifier.size(planeWidth, planeHeight)) {
        translate(left = overhang.toPx(), top = overhang.toPx()) {
            segments.forEach { segment ->
                drawRibbonSegment(
                    segment = segment,
                    progress = progress,
                    widthPx = stroke.toPx(),
                )
                if (tint > 0f) {
                    drawPath(
                        path = segment.path,
                        brush = SolidColor(groupColor),
                        alpha = tint,
                        style = Stroke(
                            width = stroke.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                    )
                }
            }
            overlay()
        }
    }
}

private fun DrawScope.drawCubicPoints(
    curve: RibbonCurve,
    index: Int,
    alpha: Float,
    ink: Color = Zinc900,
) {
    val from = curve.halfTurnBounds[index]
    val to = curve.halfTurnBounds[index + 1]
    val quarters = ceil((to - from) / QUARTER_TURN).toInt().coerceAtLeast(1)
    val step = (to - from) / quarters

    repeat(quarters) { quarter ->
        val points = ribbonCubicPoints(
            fromDegrees = from + (quarter * step),
            toDegrees = from + ((quarter + 1) * step),
            radius = curve.radius,
            centerAt = curve::centerAt,
        )
        listOf(points[0] to points[1], points[3] to points[2]).forEach { (anchor, handle) ->
            drawLine(ink.copy(alpha = .5f), anchor, handle, 2.dp.toPx(), alpha = alpha)
            drawCircle(White, radius = 4.5.dp.toPx(), center = handle, alpha = alpha)
            drawCircle(
                color = ink.copy(alpha = .7f),
                radius = 4.5.dp.toPx(),
                center = handle,
                alpha = alpha,
                style = Stroke(width = 1.5.dp.toPx()),
            )
        }
    }

    repeat(quarters + 1) { anchor ->
        val point = curve.pointAt(from + (anchor * step))
        drawCircle(White, radius = 8.dp.toPx(), center = point, alpha = alpha)
        drawCircle(ink, radius = 4.5.dp.toPx(), center = point, alpha = alpha)
    }
}

private val LayoutBoundsRed = Color(0xFFFF0000)
private val LayoutBoundsBlue = Color(0xFF3F7FFF)

private fun DrawScope.drawConstruction(
    curve: RibbonCurve,
    degrees: Float,
    alpha: Float,
    guide: Color = Zinc400,
    ink: Color = Zinc900,
) {
    val center = curve.centerAt(degrees)
    val point = curve.pointAt(degrees)
    val bounds = curve.size

    drawRect(
        color = LayoutBoundsRed,
        size = bounds,
        alpha = alpha,
        style = Stroke(width = 1.5.dp.toPx()),
    )

    val diagonal = 1.dp.toPx()
    drawLine(LayoutBoundsBlue, Offset.Zero, Offset(bounds.width, bounds.height), diagonal, alpha = alpha)
    drawLine(LayoutBoundsBlue, Offset(bounds.width, 0f), Offset(0f, bounds.height), diagonal, alpha = alpha)

    val reach = 14.dp.toPx()
    val thickness = 3.dp.toPx()
    listOf(
        Offset.Zero to Offset(1f, 1f),
        Offset(bounds.width, 0f) to Offset(-1f, 1f),
        Offset(0f, bounds.height) to Offset(1f, -1f),
        Offset(bounds.width, bounds.height) to Offset(-1f, -1f),
    ).forEach { (corner, inwards) ->
        drawRect(
            color = LayoutBoundsBlue,
            topLeft = Offset(
                x = if (inwards.x > 0f) corner.x else corner.x - reach,
                y = if (inwards.y > 0f) corner.y else corner.y - thickness,
            ),
            size = Size(reach, thickness),
            alpha = alpha,
        )
        drawRect(
            color = LayoutBoundsBlue,
            topLeft = Offset(
                x = if (inwards.x > 0f) corner.x else corner.x - thickness,
                y = if (inwards.y > 0f) corner.y else corner.y - reach,
            ),
            size = Size(thickness, reach),
            alpha = alpha,
        )
    }

    drawCircle(
        color = guide,
        radius = curve.radius,
        center = center,
        alpha = alpha,
        style = Stroke(width = 2.dp.toPx()),
    )
    drawLine(
        color = ink.copy(alpha = .45f),
        start = center,
        end = point,
        strokeWidth = 2.dp.toPx(),
        alpha = alpha,
    )
    drawCircle(ink.copy(alpha = .45f), radius = 5.dp.toPx(), center = center, alpha = alpha)
    drawCircle(White, radius = 8.dp.toPx(), center = point, alpha = alpha)
    drawCircle(ink, radius = 4.5.dp.toPx(), center = point, alpha = alpha)
}
