package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime600
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Zinc900


const val NORMAL_THUMB_SIZE = 84
const val LARGE_THUMB_SIZE = 148

internal data class ThumbCustomizationState(
    val thumbStyle: Int = 0,
    val isFlashing: Boolean = false,
    val thumbSizeDp: Int = NORMAL_THUMB_SIZE,
    val showNumber: Boolean = false,
    val rotateText: Boolean = false,
    val selectedTab: Int = 0,
)

internal class ThumbCustomizationSlide : ListSlideAdvanced<ThumbCustomizationState>() {

    override val initialState get() = ThumbCustomizationState()

    override val stateMutations
        get() = listOf<ThumbCustomizationState.() -> ThumbCustomizationState>(
            { copy(thumbStyle = 1) },
            { copy(thumbStyle = 2) },
            { copy(isFlashing = true) },
            { copy(thumbSizeDp = LARGE_THUMB_SIZE) },
            { copy(thumbStyle = -1) },
            { copy(isFlashing = false) },
            { copy(isFlashing = false, thumbSizeDp = NORMAL_THUMB_SIZE, thumbStyle = 2) },
            { copy(showNumber = true) },
            { copy(rotateText = true) },
            { copy(selectedTab = 1) },
            { copy(selectedTab = 2) },
        )

    @Composable
    override fun content(state: ThumbCustomizationState) {
        var degree by remember { mutableFloatStateOf(135f) }
        val density = LocalDensity.current
        var dialSizeDp by remember { mutableStateOf(280.dp) }

        val interactionSource = remember { MutableInteractionSource() }
        val isDragging by interactionSource.collectIsDraggedAsState()
        val isPressed by interactionSource.collectIsDraggedAsState()

        val animatedThumbSize by animateDpAsState(
            targetValue = state.thumbSizeDp.dp,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
//                dampingRatio = Spring.DampingRatioLowBouncy,
            )
        )

        val draggingThumbSize by animateFloatAsState(
            targetValue = if (isDragging || isPressed) .8f else 1f,
            animationSpec = spring(
                stiffness = Spring.StiffnessMedium,
                dampingRatio = Spring.DampingRatioMediumBouncy,
            )
        )

        val borderAlpha = remember { Animatable(0f) }
        LaunchedEffect(state.isFlashing) {
            if (state.isFlashing) {
                borderAlpha.animateTo(0.5f, tween(300))
                while (true) {
                    borderAlpha.animateTo(1.0f, tween(600))
                    borderAlpha.animateTo(0.5f, tween(600))
                }
            } else {
                borderAlpha.animateTo(0f, tween(300))
            }
        }

        val customThumbCode = buildCustomThumbCode(state)

        val interactionSourceCode = """
val interactionSource = remember {
    MutableInteractionSource()
}
val isDragging by interactionSource
    .collectIsDraggedAsState()
    
Dial(
    degree = degree,
    onDegreeChange = { degree = it },
    interactionSource = interactionSource,
    thumb = { ... },
    track = { ... },
)
        """.trimIndent()

        val draggingThumbCode = """
val thumbScale by animateFloatAsState(
    targetValue = if (isDragging) .8f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
    ),
)

Dial(
    ...
    interactionSource = interactionSource,
    thumb = { _ ->
        Canvas(
            modifier = Modifier
                .size(84.dp)
                .scale(thumbScale)
        ) {
            ...
        }
    },
    track = { ... },
)
        """.trimIndent()

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier.weight(1.4f),
                tabs = listOf(
                    "CustomThumb.kt" to customThumbCode,
                    "InteractionSource.kt" to interactionSourceCode,
                    "DraggingThumb.kt" to draggingThumbCode,
                ),
                selectedTab = state.selectedTab,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Dial(
                    degree = degree,
                    onDegreeChange = { degree = it },
                    modifier = Modifier
                        .size(400.dp)
                        .onSizeChanged { dialSizeDp = with(density) { it.width.toDp() } },
                    startDegrees = 225f,
                    sweepDegrees = 270f,
                    interactionSource = interactionSource,
                    thumb = { dialState ->
                        val effectiveSize = draggingThumbSize

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(animatedThumbSize)
                                .scale(effectiveSize)
                                .drawBehind {
                                    drawThumbBorder(borderAlpha.value)
                                }
                                .padding(4.dp)
                        ) {
                            when (state.thumbStyle) {
                                -1 -> {}
                                0 -> Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(color = Zinc900)
                                    drawCircle(
                                        brush = Brush.sweepGradient(
                                            listOf(Lime400, Sky400, Rose400, Violet400, Lime400)
                                        ),
                                        style = Stroke(width = 3.dp.toPx()),
                                    )
                                }

                                1 -> Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawRoundRect(
                                        brush = Brush.radialGradient(
                                            listOf(Lime400, Lime600)
                                        ),
                                        cornerRadius = CornerRadius(8.dp.toPx()),
                                    )
                                    drawRoundRect(
                                        color = Lime300,
                                        cornerRadius = CornerRadius(8.dp.toPx()),
                                        style = Stroke(width = 1.5.dp.toPx()),
                                    )
                                }

                                else -> Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(color = Sky400)
                                    drawCircle(
                                        color = Zinc900,
                                        radius = size.minDimension / 2 - 4.dp.toPx()
                                    )
                                }
                            }

                            if (state.showNumber) {
                                Text(
                                    text = "${(degree / 270f * 100).toInt()}",
                                    color = Lime400,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .then(
                                            if (state.rotateText) {
                                                Modifier.rotate(
                                                    -(dialState.absoluteDegree + dialState.overshootDegrees)
                                                )
                                            } else Modifier
                                        )
                                )
                            }
                        }
                    },
                    track = { dialState ->
                        val trackThumbSize = animatedThumbSize
                        Box(
                            Modifier
                                .fillMaxSize()
                                .drawBehind {
                                    drawArc(
                                        color = Black.copy(alpha = .9f),
                                        radius = dialState.radius - (trackThumbSize.toPx() / 2),
                                        startAngle = dialState.startDegrees,
                                        sweepAngle = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                                        strokeWidth = 24.dp,
                                    )
                                    drawArc(
                                        color = Lime500,
                                        radius = dialState.radius - (trackThumbSize.toPx() / 2),
                                        startAngle = dialState.startDegrees + dialState.overshootDegrees.coerceAtMost(
                                            0f
                                        ),
                                        sweepAngle = dialState.degree + dialState.overshootDegrees,
                                        strokeWidth = 16.dp,
                                    )
                                }
                        )
                    },
                )
            }
        }
    }
}

private fun DrawScope.drawThumbBorder(alpha: Float) {
    if (alpha <= 0f) return
    val cornerLen = size.minDimension * 0.1f
    val strokeThin = 1.dp.toPx()
    val strokeThick = 4.dp.toPx()
    val inset = strokeThick / 2f
    val r = size.width - inset
    val b = size.height - inset

    drawRect(
        brush = Brush.radialGradient(
            listOf(Lime400.copy(alpha = 0.1f * alpha), Lime400.copy(alpha = 0f))
        ),
    )
    drawRect(
        color = Lime400.copy(alpha = alpha * 0.6f),
        topLeft = Offset(inset, inset),
        size = Size(size.width - inset * 2, size.height - inset * 2),
        style = Stroke(width = strokeThin),
    )
    // top-left
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(inset, inset + cornerLen),
        Offset(inset, inset),
        strokeThick
    )
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(inset, inset),
        Offset(inset + cornerLen, inset),
        strokeThick
    )
    // top-right
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(r - cornerLen, inset),
        Offset(r, inset),
        strokeThick
    )
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(r, inset),
        Offset(r, inset + cornerLen),
        strokeThick
    )
    // bottom-left
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(inset, b - cornerLen),
        Offset(inset, b),
        strokeThick
    )
    drawLine(
        Lime400.copy(alpha = alpha),
        Offset(inset, b),
        Offset(inset + cornerLen, b),
        strokeThick
    )
    // bottom-right
    drawLine(Lime400.copy(alpha = alpha), Offset(r - cornerLen, b), Offset(r, b), strokeThick)
    drawLine(Lime400.copy(alpha = alpha), Offset(r, b - cornerLen), Offset(r, b), strokeThick)
}

private fun buildCustomThumbCode(state: ThumbCustomizationState): String {
    val size = "Modifier.size(${state.thumbSizeDp}.dp)"

    val thumbBody = when (state.thumbStyle) {
        0 -> """
Canvas(
    modifier = $size
) {
    drawCircle(
        color = Zinc900,
    )
    drawCircle(
        brush = Brush.sweepGradient(
            listOf(
                Lime400,
                Sky400,
                Rose400,
                Violet400,
                Lime400,
            )
        ),
        style = Stroke(
            width = 3.dp.toPx()
        ),
    )
}""".trimIndent()

        1 -> """
Canvas(
    modifier = $size
) {
    drawRoundRect(
        brush = Brush.radialGradient(
            listOf(Lime400, Lime600)
        ),
        cornerRadius = CornerRadius(8.dp.toPx()),
    )
    drawRoundRect(
        color = Lime300,
        cornerRadius = CornerRadius(8.dp.toPx()),
        style = Stroke(width = 1.5.dp.toPx()),
    )
}""".trimIndent()

        else -> {
            val rotateBlock = if (state.rotateText) """
        .rotate(
            -(dialState.absoluteDegree
                + dialState.overshootDegrees)
        )""" else ""

            val numberLine = if (state.showNumber) """

Text(
    text = "${"\$"}{value}",
    color = Lime400,
    modifier = Modifier$rotateBlock,
)""" else ""

            """
Canvas(
    modifier = $size
) {
    drawCircle(color = Sky400)
    drawCircle(
        color = Zinc900,
        radius = size.minDimension / 2 - 4.dp.toPx()
    )
}$numberLine""".trimIndent()
        }
    }

    val lambdaParam = if (state.showNumber || state.rotateText) "dialState" else "_"

    return """
Dial(
    degree = degree,
    onDegreeChange = { degree = it },
    thumb = { $lambdaParam ->
${thumbBody.prependIndent("        ")}
    },
)""".trimIndent()
}
