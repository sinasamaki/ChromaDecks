package com.sinasamaki.chromadecks._006_SpinAnimation.slides

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._006_SpinAnimation.RedLook
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinGround
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinRed
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinRedDim
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinBody
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.AccordionText
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax

private const val TITLE = "spin button"

private const val START_ANGLE = -420f

private const val START_TILT = -7f

private const val START_X = .16f
private const val START_Y = -.2f

private val Drop = tween<Float>(durationMillis = 1500, easing = LinearEasing)

private val Settle = tween<Color>(durationMillis = 700)

private val PadX = 56.dp
private val PadY = 28.dp

private val ButtonThickness = 32.dp

internal data class SpinTitleState(
    val landed: Boolean,
)

internal class SpinTitleSlide : ListSlideAdvanced<SpinTitleState>() {

    override val initialState get() = SpinTitleState(landed = false)

    override val stateMutations: List<SpinTitleState.() -> SpinTitleState>
        get() = listOf(
            { copy(landed = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(.9f).fadeIn().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: SpinTitleState) {
        val drop = remember { Animatable(0f) }
        var cut by remember { mutableStateOf(false) }
        LaunchedEffect(state.landed) {
            if (!state.landed) {
                cut = false
                drop.snapTo(0f)
                return@LaunchedEffect
            }
            drop.animateTo(targetValue = 1f, animationSpec = Drop)
            cut = true
        }

        val cardInk = remember { Animatable(SpinRedDim) }
        LaunchedEffect(cut) {
            cardInk.snapTo(SpinRedDim)
            if (cut) cardInk.animateTo(SpinRed, Settle)
        }

        val anchor = remember { TitleAnchor() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onPlaced { anchor.slide = it }
                .background(if (cut) SpinGround else RedLook.front),
        ) {
            TitleFrame(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = if (cut) 1f else 0f },
                title = TITLE,
                description = "a flat pill, faking its way into three dimensions",
                hint = "from SubAtomic",
                bookNumber = 6,
                contentColor = cardInk.value,
                titleModifier = Modifier
                    .onPlaced { anchor.title = it }
                    .tint(SpinRed),
            )

            if (!cut) {
                TitleButton(anchor = anchor, drop = { drop.value })
            }
        }
    }
}

private class TitleAnchor {
    var slide by mutableStateOf<LayoutCoordinates?>(null)
    var title by mutableStateOf<LayoutCoordinates?>(null)
}

@Composable
private fun TitleButton(anchor: TitleAnchor, drop: () -> Float) {
    val slide = anchor.slide?.takeIf { it.isAttached } ?: return
    val title = anchor.title?.takeIf { it.isAttached } ?: return

    val density = LocalDensity.current
    val padX = with(density) { PadX.roundToPx() }
    val padY = with(density) { PadY.roundToPx() }
    val width = title.size.width + padX * 2
    val height = title.size.height + padY * 2
    val restingAt = slide.localPositionOf(title, Offset.Zero) - Offset(padX.toFloat(), padY.toFloat())

    SpinBody(
        angle = { lerp(START_ANGLE, 0f, drop()) },
        modifier = Modifier
            .graphicsLayer {
                val away = 1f - drop()
                translationX = restingAt.x + away * START_X * slide.size.width
                translationY = restingAt.y + away * START_Y * slide.size.height
                rotationZ = away * START_TILT
            }
            .size(with(density) { width.toDp() }, with(density) { height.toDp() }),
        thickness = ButtonThickness,
    ) {
        AccordionText(
            text = TITLE,
            animationProgress = 1f,
            modifier = Modifier.tint(RedLook.ink),
        )
    }
}

private fun Modifier.tint(color: Color): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawRect(color = color, blendMode = BlendMode.SrcIn)
    }
