package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.drawScaledPath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.pxDp
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Emerald200
import com.sinasamaki.chromadecks.ui.theme.Emerald400
import com.sinasamaki.chromadecks.ui.theme.Emerald50
import com.sinasamaki.chromadecks.ui.theme.Green50
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlinx.coroutines.launch

internal data class StampPathEffectState(
    val useArrow: Boolean = false,
    val showBackgroundPath: Boolean = false,
    val showBackgroundStamps: Boolean = false,
    val stampedEffectStyle: StampedPathEffectStyle = StampedPathEffectStyle.Translate
)

internal class StampPathEffect : ListSlideAdvanced<StampPathEffectState>() {

    override val initialState: StampPathEffectState
        get() = StampPathEffectState()

    override val stateMutations: List<StampPathEffectState.() -> StampPathEffectState>
        get() = listOf(
            { copy(showBackgroundPath = true) },
            { copy(showBackgroundStamps = true) },
            {
                copy(
                    showBackgroundPath = false,
                    showBackgroundStamps = false,
                )
            },
            { copy(useArrow = true) },
            { copy(stampedEffectStyle = StampedPathEffectStyle.Rotate) },
            { copy(stampedEffectStyle = StampedPathEffectStyle.Morph) },
        )

    @Composable
    override fun content(state: StampPathEffectState) {


        val backgroundPathAnimation by animateFloatAsState(
            targetValue = if (state.showBackgroundPath) 0f else 1f,
            animationSpec = tween(1000, easing = LinearEasing)
        )

        val backgroundStampAnimation by animateFloatAsState(
            targetValue = if (state.showBackgroundStamps) 0f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )

        val foregroundBlurAnimation by animateFloatAsState(
            targetValue = if (state.showBackgroundStamps || state.showBackgroundPath) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium)
        )

        Box(
            Modifier
                .drawBehind {

                    val cross = Path().apply {
                        val crossWidth = 5f
                        val crossLength = 15f
                        addRect(
                            rect = Rect(
                                topLeft = Offset(-crossWidth, -crossLength),
                                bottomRight = Offset(crossWidth, crossLength)
                            )
                        )

                        addRect(
                            rect = Rect(
                                topLeft = Offset(-crossLength, -crossWidth),
                                bottomRight = Offset(crossLength, crossWidth)
                            )
                        )
                    }

                    val cols = 4 * 4
                    val rows = 3 * 4
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        for (i in 0..rows) {
                            relativeLineTo(
                                size.width * if (i % 2 == 0) 1 else -1,
                                0f,
                            )
                            relativeLineTo(
                                0f,
                                size.height / rows
                            )
                        }
                    }
                    val measure = PathMeasure()
                    measure.setPath(path, false)
                    scale(
                        pivot = center,
                        scale = .9f
                    ) {
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(
                                listOf(Emerald50, Green50)
                            ),
                            alpha = .4f,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        measure.length * (1f - backgroundPathAnimation),
                                        measure.length
                                    )
                                )
                            )
                        )
                        drawPath(
                            path = path,
                            color = lerp(
                                Amber400,
                                Slate50.copy(alpha = .04f),
                                backgroundStampAnimation
                            ),
                            style = Stroke(
                                pathEffect = PathEffect.stampedPathEffect(
                                    shape = cross,
                                    advance = size.width / cols,
                                    phase = 0f,
                                    style = StampedPathEffectStyle.Translate
                                )
                            )
                        )
                    }

                }
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .blur(
                    radius = androidx.compose.ui.unit.lerp(
                        0.dp,
                        50.dp,
                        foregroundBlurAnimation
                    )
                )
                .alpha(
                    androidx.compose.ui.util.lerp(
                        1f,
                        .7f,
                        foregroundBlurAnimation
                    )
                )
        ) {

            Row(
                modifier = Modifier
                    .padding(64.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                val sourcePath by remember(state.useArrow) {
                    derivedStateOf {
                        if (state.useArrow) {
                            Path().apply {
                                moveTo(0f, -30f)
                                lineTo(0f, 30f)
                                lineTo(30f, 0f)

                                moveTo(0f, 0f)
                                relativeLineTo(0f, -5f)
                                relativeLineTo(-50f, 0f)
                                relativeLineTo(0f, 10f)
                                relativeLineTo(50f, 0f)
                            }
                        } else {
                            Path().apply {
                                addOval(
                                    oval = Rect(
                                        center = Offset.Zero,
                                        radius = 40f,
                                    )
                                )
                            }
                        }
                    }
                }
                val path by remember {
                    derivedStateOf {
                        Path().apply {
                            this.moveTo(10f, 75f)
                            this.quadraticTo(
                                50f, -50f,
                                90f, 75f,
                            )
                        }
                    }
                }

                val brush = remember {
                    Brush.verticalGradient(
                        colors = listOf(Emerald400, Yellow400)
                    )
                }
                var codeDisplayWidth by remember { mutableStateOf(0) }
                PathCodeDisplay(
                    modifier = Modifier
                        .onSizeChanged { codeDisplayWidth = it.width }
                        .weight(.8f),
                    code = """
                            val path = Path().apply
                                moveTo(10f, 75f)
                                quadraticTo(
                                    50f, -50f,
                                    90f, 75f,
                                )
                            }
                        """.trimIndent()
                ) {
                    drawScaledPath(
                        path = path.copy(),
                        brush = brush,
                        style = Stroke(
                            width = 10f,
                        )
                    )
                }

                Text(
                    text = "+",
                    modifier = Modifier.padding(top = (codeDisplayWidth / 2).pxDp),
                    color = Emerald400
                )

                PathCodeDisplay(
                    modifier = Modifier.weight(.8f),
                    code = if (state.useArrow) """
                        val shape = Path().apply {
                            moveTo(0f, -30f)
                            lineTo(0f, 30f)
                            lineTo(30f, 0f)

                            moveTo(0f, 0f)
                            relativeLineTo(0f, -5f)
                            relativeLineTo(-50f, 0f)
                            relativeLineTo(0f, 10f)
                            relativeLineTo(50f, 0f)
                        }
                        """.trimIndent()
                    else """
                        val shape = Path().apply {
                            addOval(
                                oval = Rect(
                                    Offset.Zero,
                                    40f,
                                )
                            )
                        }
                    """.trimIndent()
                ) {
                    translate(
                        left = size.width / 2,
                        top = size.height / 2,
                    ) {

                        drawPath(
                            path = sourcePath,
                            brush = brush,
                        )
                    }
                }


                Text(
                    text = "=",
                    modifier = Modifier.padding(top = (codeDisplayWidth / 2).pxDp),
                    color = Emerald400
                )

                Column(
                    modifier = Modifier.weight(.865f),
                ) {

                    var advance by remember { mutableStateOf(85f) }
                    var phase by remember { mutableStateOf(0f) }

                    PathCodeDisplay(
                        code = """
                            PathEffect.stampedPathEffect(
                                shape = shape,
                                advance = ${advance.toInt()}f,
                                phase = ${phase.toInt()}f,
                                style = ${state.stampedEffectStyle}
                            )
                        """.trimIndent()
                    ) {
                        drawScaledPath(
                            path = path,
                            brush = brush,
                            style = Stroke(
                                pathEffect = PathEffect.stampedPathEffect(
                                    shape = sourcePath,
                                    advance = advance,
                                    phase = phase,
                                    style = state.stampedEffectStyle
                                )
                            )
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    SliderWithLabel(
                        modifier = Modifier.weight(1f),
                        label = "Advance",
                        value = advance,
                        onValueChange = { advance = it },
                        valueRange = 1f..200f,
                    )

                    SliderWithLabel(
                        modifier = Modifier.weight(1f),
                        label = "Phase",
                        value = phase,
                        onValueChange = { phase = it },
                        valueRange = 1f..500f,
                    )
                }
            }
        }


    }
}


@Composable
private fun PathCodeDisplay(
    modifier: Modifier = Modifier,
    code: String,
    draw: DrawScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .frame()
                .drawBehind(draw)
        )

        Spacer(Modifier.height(32.dp))

        CodeBlock(
            code = code,
            modifier = Modifier.fillMaxWidth(),
            fadeAnimations = false
        )
    }


}

@Composable
fun Modifier.frame(): Modifier {
    val animation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    return this
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    scope.launch {
                        animation.animateTo(1f)
                        animation.animateTo(
                            0f,
                            spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                }
            )
        }
        .aspectRatio(1f)
        .offset {
            IntOffset(
                x = 0,
                y = androidx.compose.ui.util.lerp(0, -20, animation.value)
            )
        }
        .scale(
            scale = androidx.compose.ui.util.lerp(1f, 1.01f, animation.value)
        )
        .background(color = Zinc950, shape = RoundedCornerShape(48.dp))
        .border(
            width = 1.dp,
            color = Emerald200,
            shape = RoundedCornerShape(48.dp),
        )
        .scale(
            scale = androidx.compose.ui.util.lerp(1f, 0.99f, animation.value)
        )
}