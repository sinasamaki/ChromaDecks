package com.sinasamaki.chromadecks.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * Scope for [ExplodedView]'s content. Emit each plane with [layer]; every layer is run through the
 * view's `decorator`, so callers get a single hook to frame/shadow/animate all the planes at once.
 */
@Stable
class ExplodedViewScope internal constructor(
    private val decorator: @Composable (content: @Composable () -> Unit) -> Unit,
) {
    /** One plane of the exploded stack, wrapped in the view's decorator. */
    @Composable
    fun layer(content: @Composable () -> Unit) {
        decorator(content)
    }
}

/**
 * Stacks every [layer][ExplodedViewScope.layer] of [content] on the same spot, then fans them out
 * into depth like an exploded-view diagram.
 *
 * All layers share the same [rotationX]/[rotationY]/[rotationZ], so the planes stay parallel, and
 * each sits [spacing] further along the stack's z-axis than the one before it. Each layer's
 * on-screen shift is found by mapping a pure z-translation through the shared rotation [Matrix], so
 * the layers separate along the tilted normal rather than straight down the screen — collapse
 * [spacing] to `0.dp` with zero rotation and the stack reads as a single flat view.
 *
 * [decorator] wraps every layer, giving one place to frame the planes (a border, an inner shadow…)
 * and animate that framing in as the stack explodes.
 */
@Composable
fun ExplodedView(
    rotationX: Float,
    rotationY: Float,
    rotationZ: Float,
    spacing: Dp,
    modifier: Modifier = Modifier,
    cameraDistance: Float = 16f,
    decorator: @Composable (content: @Composable () -> Unit) -> Unit = { it() },
    content: @Composable ExplodedViewScope.() -> Unit,
) {
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }

    val scope = remember(decorator) { ExplodedViewScope(decorator) }

    // The rotation shared by every layer, rebuilt only when an angle changes. We also reuse it to
    // project each layer's z-offset onto the screen so the fan-out tracks the tilt.
    val matrix = remember(rotationX, rotationY, rotationZ) {
        Matrix().apply {
            rotateX(rotationX)
            rotateY(rotationY)
            rotateZ(rotationZ)
        }
    }
    // Column 2 of the rotation maps an input z onto output x/y (Matrix values are 4x4, row/col
    // indexed as row * 4 + col — indices 8 and 9 are the z→x and z→y coefficients).
    val zToX = matrix.values[8]
    val zToY = matrix.values[9]

    Layout(
        content = { scope.content() },
        modifier = modifier,
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val width = placeables.maxOfOrNull { it.width } ?: constraints.minWidth
        val height = placeables.maxOfOrNull { it.height } ?: constraints.minHeight

        // Middle layer sits at z=0; the rest split evenly above and below it.
        val centerIndex = (placeables.size - 1) / 2f

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val z = (index - centerIndex) * spacingPx
                placeable.placeWithLayer(
                    x = (width - placeable.width) / 2,
                    y = (height - placeable.height) / 2,
                ) {
                    this.rotationX = rotationX
                    this.rotationY = rotationY
                    this.rotationZ = rotationZ
                    this.cameraDistance = cameraDistance
                    // Slide the layer along the tilted z-axis, projected back onto the screen.
                    translationX = zToX * z
                    translationY = zToY * z
                    transformOrigin = TransformOrigin.Center
                    this.cameraDistance = 50f
                }
            }
        }
    }
}
