package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.EaseInSine
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.RIBBON_COLORS
import com.sinasamaki.chromadecks._005_RibbonModifier.components.leadRibbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.slideanimations.blurIn
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc900

private const val FRAME_MILLIS = 2400

private const val TAIL_END = 1f

internal data class RibbonTitleState(
    val wrapped: Boolean,
)

internal class RibbonTitleSlide : ListSlideAdvanced<RibbonTitleState>() {

    override val initialState get() = RibbonTitleState(wrapped = false)

    override val stateMutations: List<RibbonTitleState.() -> RibbonTitleState>
        get() = listOf(
            { copy(wrapped = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(
                Modifier
                    .parallax(.9f)
                    .fadeIn()
                    .blurOut(initial = 200f)
                    .fadeOut()
                    .blurIn(initial = 100f)
            ) {
                content()
            }
        }

    @Composable
    override fun content(state: RibbonTitleState) {
        val progress = animateFloatAsState(
            targetValue = if (state.wrapped) 1f else .1f,
//            animationSpec = tween(durationMillis = 3400, easing = FastOutSlowInEasing),
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow * .01f,
            ),
            visibilityThreshold = .00001f,
            label = "title-ribbon",
        )
        val tail = animateFloatAsState(
            targetValue = if (state.wrapped) TAIL_END else 0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow * .0035f,
            ),
            visibilityThreshold = .00001f,
            label = "title-ribbon-tail",
        )

        val offset = animateFloatAsState(
            targetValue = if (state.wrapped) 0f else .9f,
//            animationSpec = tween(durationMillis = FRAME_MILLIS, easing = FastOutSlowInEasing),
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow * .1f,
            ),
            visibilityThreshold = .00001f,
            label = "title-offset",
        )
        val scale = animateFloatAsState(
            targetValue = if (state.wrapped) 1f else 1.9f,
//            animationSpec = tween(durationMillis = FRAME_MILLIS * 2, easing = FastOutSlowInEasing),
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow * .05f,
            ),
            visibilityThreshold = .00001f,
            label = "title-scale",
        )

        val anchor = remember { TitleAnchor() }

        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = offset.value * size.width
                        scaleX = scale.value
                        scaleY = scale.value
//                        transformOrigin = TransformOrigin(-1.5f, .5f)
                    }
            ) {
                TitleFrame(
                    modifier = Modifier
                        .fillMaxSize()
                        .grid(),
                    title = "ribbon modifier",
                    description = "a ribbon that wraps itself around any composable",
                    hint = "from SubAtomic",
                    bookNumber = 5,
                    contentColor = Zinc900,
                    showDivider = false,
                    titleModifier = Modifier
                        .onPlaced { anchor.coordinates = it }
                        .leadRibbon(
                            colors = RIBBON_COLORS,
                            stroke = 48.dp,
                            loops = 4,
                            leadIn = anchor::leadIn,
                            leadOut = anchor::leadOut,
                            progress = { progress.value },
                            tail = { tail.value },
                        )
//                        .shadow(
//                            elevation = 10.dp,
//                            shape = CircleShape,
//                            spotColor = Black.copy(alpha = .2f)
//                        )
                        .background(
                            color = Zinc50.copy(alpha = .99f),
                            shape = CircleShape,
                        )
                        .padding(16.dp)
                    ,
                )
            }
        }
    }
}

private class TitleAnchor {
    var coordinates: LayoutCoordinates? = null

    fun leadIn(): Float {
        val title = coordinates?.takeIf { it.isAttached } ?: return 0f
        return -title.localPositionOf(title.findRootCoordinates(), Offset.Zero).x
    }

    fun leadOut(): Float {
        val title = coordinates?.takeIf { it.isAttached } ?: return 0f
        val root = title.findRootCoordinates()
        val edge = title.localPositionOf(root, Offset(root.size.width.toFloat(), 0f)).x
        return edge - title.size.width
    }
}

private val GRID_CELL = 48.dp
private val GRID_LINE = 1.dp
private val GRID_COLOR = Zinc200

private fun Modifier.grid(): Modifier = drawWithCache {
    val cell = GRID_CELL.toPx()
    val line = GRID_LINE.toPx() / cell
    val stops = arrayOf(
        0f to GRID_COLOR,
        line to GRID_COLOR,
        line to Color.Transparent,
        1f to Color.Transparent,
    )
    val rows = Brush.verticalGradient(
        colorStops = stops,
        startY = 0f,
        endY = cell,
        tileMode = TileMode.Repeated,
    )
    val columns = Brush.horizontalGradient(
        colorStops = stops,
        startX = 0f,
        endX = cell,
        tileMode = TileMode.Repeated,
    )
    val overscan = Offset(-size.width, -size.height)
    val area = Size(size.width * 3f, size.height * 3f)

    onDrawBehind {
        drawRect(brush = rows, topLeft = overscan, size = area)
        drawRect(brush = columns, topLeft = overscan, size = area)
    }
}
