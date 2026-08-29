package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Cyan500
import com.sinasamaki.chromadecks.ui.theme.Red500
import kotlin.math.sqrt

/**
 * Zooms into [pivot] (normalised 0f..1f in each axis) of [content] by [zoom], keeping that point
 * parked in the centre of the viewport as it scales.
 *
 * A radial mask vignettes everything outside a circle whose diameter equals the viewport's
 * diagonal: within that circle the content is fully opaque, beyond it the radial gradient fades to
 * transparent. At [zoom] `1f` with a centred pivot the fully-visible circle reaches the corners, so
 * the mask is invisible — it only reveals itself as you zoom in and content spills past the edge.
 * The mask is composited via [layer] so the `DstIn` blend applies to the content as one group.
 */
@Composable
fun FocusZoomer(
    zoom: Float,
    pivot: Offset,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow,
        visibilityThreshold = .0001f
    ),
    content: @Composable () -> Unit,
) {
    val animatedZoom by animateFloatAsState(
        targetValue = zoom,
        animationSpec,
        label = "focusZoom",
        visibilityThreshold = .0001f
    )
    val pivotX by animateFloatAsState(
        targetValue = pivot.x,
        animationSpec,
        label = "focusPivotX",
        visibilityThreshold = .0001f
    )
    val pivotY by animateFloatAsState(
        targetValue = pivot.y,
        animationSpec,
        label = "focusPivotY",
        visibilityThreshold = .0001f
    )

    Box(
        modifier = modifier
            .drawWithContent {
                val diagonal = sqrt(size.width * size.width + size.height * size.height)
                layer(size.toRect().inflate(diagonal * 1f)) {
                    this@drawWithContent.drawContent()
                    drawCircle(
                        brush = Brush.radialGradient(
                            0f to Color.Red,
                            .5f to Color.Red,
                            1f to Color.Transparent,
                            center = center,
                            radius = diagonal * .5f,
                        ),
                        radius = diagonal,
                        blendMode = BlendMode.DstIn,
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    clip = true
                    val inflateLength = size.width
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Rectangle(
                                rect = size.toRect().inflate(inflateLength)
                            )
                        }
                    }
                }
                .graphicsLayer {
                    scaleX = animatedZoom
                    scaleY = animatedZoom
                    transformOrigin = TransformOrigin(pivotX, pivotY)
                    translationX = (.5f - pivotX) * size.width
                    translationY = (.5f - pivotY) * size.height
                },
        ) {
            content()
        }
    }
}
