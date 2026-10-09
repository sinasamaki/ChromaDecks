package com.sinasamaki.chromadecks._006_SpinAnimation.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseInSine
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._006_SpinAnimation.RedLook
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinLook
import com.sinasamaki.chromadecks.ui.theme.Black

internal const val NEAR_CAMERA = 8f

internal const val FAR_CAMERA = 200f

private val BorderWidth = 3.dp

private const val BACK_DARKEN = .5f

internal enum class BackStyle {
    Mirrored,

    Darkened,

    Swapped,

    Tinted,
}

internal data class SpinStage(
    val border: Float = 1f,
    val cameraDistance: Float = FAR_CAMERA,
    val faceAlpha: Float = 1f,
    val faceShade: Float = 1f,
    val thickness: Float = 1f,
    val strokeScale: Float = 1f,
    val eased: Float = 1f,
    val clip: Boolean = true,
    val sideGradient: Float = 1f,
    val sideShade: Float = 1f,
    val hop: Float = 1f,
    val back: BackStyle = BackStyle.Tinted,
) {
    companion object {
        val Flat = SpinStage(
            faceShade = 0f,
            thickness = 0f,
            eased = 0f,
            clip = false,
            sideGradient = 0f,
            sideShade = 0f,
            hop = 0f,
            back = BackStyle.Mirrored,
        )

        val Full = SpinStage()
    }
}

@Composable
internal fun animateSpinStage(target: SpinStage): SpinStage {
    @Composable
    fun animate(value: Float, label: String): Float {
        val animated by animateFloatAsState(
            targetValue = value,
            animationSpec = spring(stiffness = Spring.StiffnessLow, visibilityThreshold = .0001f),
            label = label,
        )
        return animated
    }

    return target.copy(
        border = animate(target.border, "border"),
        cameraDistance = animate(target.cameraDistance, "camera"),
        faceAlpha = animate(target.faceAlpha, "faceAlpha"),
        faceShade = animate(target.faceShade, "faceShade"),
        thickness = animate(target.thickness, "thickness"),
        strokeScale = animate(target.strokeScale, "strokeScale"),
        eased = animate(target.eased, "eased"),
        sideGradient = animate(target.sideGradient, "sideGradient"),
        sideShade = animate(target.sideShade, "sideShade"),
        hop = animate(target.hop, "hop"),
    )
}

internal fun Float.showsBack(): Boolean = mod(360f) in 90f..270f

internal fun Float.byHalfTurn(flat: Float, edgeOn: Float): Float {
    val halfTurn = mod(180f)
    return if (halfTurn < 90f) {
        lerp(flat, edgeOn, EaseInSine.transform(halfTurn / 90f))
    } else {
        lerp(edgeOn, flat, EaseOutSine.transform((halfTurn - 90f) / 90f))
    }
}

private fun sidePath(size: Size, bottom: Float): Path {
    val mid = size.height / 2
    return Path().apply {
        moveTo(0f, mid)
        cubicTo(0f, bottom, mid, bottom, mid, bottom)
        lineTo(size.width - mid, bottom)
        cubicTo(size.width, bottom, size.width, mid, size.width, mid)
    }
}

@Composable
internal fun SpinBody(
    angle: () -> Float,
    modifier: Modifier = Modifier,
    stage: SpinStage = SpinStage.Full,
    look: SpinLook = RedLook,
    thickness: Dp = 24.dp,
    backContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val showsBack by remember { derivedStateOf { angle().showsBack() } }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(if (stage.clip) Modifier.clip(CircleShape) else Modifier)
                .drawBehind {
                    if (stage.thickness <= 0f) return@drawBehind
                    val turn = angle()
                    val linear = turn.mod(180f) / 180f
                    val progress = lerp(linear, EaseInOutSine.transform(linear), stage.eased)
                    val path = sidePath(size, bottom = size.height * (1f - progress))
                    val stroke = Stroke(
                        width = thickness.toPx() * stage.strokeScale,
                        cap = StrokeCap.Round,
                    )

                    val edge = (size.height / 2) / size.width
                    val ends = lerp(look.side, look.sideEnds, stage.sideGradient)
                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(
                            0f to ends,
                            edge to look.side,
                            1f - edge to look.side,
                            1f to ends,
                        ),
                        alpha = stage.thickness,
                        style = stroke,
                    )
                    drawPath(
                        path = path,
                        color = Black.copy(
                            alpha = turn.byHalfTurn(.4f, 0f) * stage.sideShade * stage.thickness,
                        ),
                        style = stroke,
                    )
                },
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val turn = angle()
                    val halfTurn = turn.mod(180f)
                    val hop = thickness.toPx() * stage.strokeScale / 2f * stage.hop
                    translationY = if (halfTurn < 90f) {
                        lerp(0f, -hop, halfTurn / 90f)
                    } else {
                        lerp(hop, 0f, (halfTurn - 90f) / 90f)
                    }
                    rotationX = turn
                    cameraDistance = stage.cameraDistance
                    alpha = stage.faceAlpha
                }
                .drawBehind {
                    val tinted = stage.back == BackStyle.Tinted && angle().showsBack()
                    drawRoundRect(
                        color = if (tinted) look.back else look.front,
                        cornerRadius = CornerRadius(size.height / 2f),
                    )
                    val width = BorderWidth.toPx()
                    if (stage.border <= 0f || size.minDimension <= width) return@drawBehind
                    inset(width / 2f) {
                        drawRoundRect(
                            brush = Brush.sweepGradient(look.edge),
                            cornerRadius = CornerRadius(size.height / 2f),
                            alpha = stage.border,
                            style = Stroke(width),
                        )
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides look.ink) {
                if (showsBack && stage.back == BackStyle.Swapped) {
                    Box(Modifier.graphicsLayer { rotationX = 180f }) { backContent() }
                } else {
                    Box(
                        Modifier.graphicsLayer {
                            alpha = if (showsBack && stage.back == BackStyle.Tinted) .1f else 1f
                        },
                    ) { content() }
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .drawBehind {
                        val turn = angle()
                        val darken = if (stage.back == BackStyle.Darkened && turn.showsBack()) {
                            BACK_DARKEN
                        } else {
                            0f
                        }
                        val shade = turn.byHalfTurn(0f, .8f) * stage.faceShade
                        drawRect(color = Black.copy(alpha = 1f - (1f - shade) * (1f - darken)))
                    },
            )
        }
    }
}
