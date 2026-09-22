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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.components.ExplodedView
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Zinc400

/**
 * The ribbon taken apart into the layers the modifier actually draws: everything that goes
 * behind on one plane, the wrapped content on the plane in the middle, everything that comes in
 * front on a third.
 *
 * The three planes are handed to [ExplodedView] in draw order, so collapsing [spacing] back to
 * zero lands them exactly where the modifier puts them.
 *
 * @param sandwiched false moves every half turn onto the front plane, which is the ribbon drawn
 *   in a single pass — the stage where it still looks like a spiral laid over the content.
 * @param tint blends each plane's ribbon towards a colour for its group, so front and behind
 *   stay readable while the stack is open.
 */
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

    val segments = remember(width, height, stroke, loops, colors, density) {
        with(density) {
            ribbonSegments(
                size = Size(width.toPx(), height.toPx()),
                strokePx = stroke.toPx(),
                loops = loops - .5f,
                colors = colors,
            )
        }
    }

    // Each plane is the content box plus the room the loop needs to clear its edges.
    val overhang = (height / 2) + stroke
    val planeWidth = width + (overhang * 2)
    val planeHeight = height + (overhang * 2)

    val behind = if (sandwiched) segments.filter { !it.inFront } else emptyList()
    val front = if (sandwiched) segments.filter { it.inFront } else segments

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
            SegmentPlane(behind, planeWidth, planeHeight, overhang, stroke, progress, tint, behindColor)
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
            SegmentPlane(front, planeWidth, planeHeight, overhang, stroke, progress, tint, frontColor)
        }
    }
}

/** Every half turn belonging to one side of the content, on one plane. */
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
        }
    }
}
