package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.fastJoinToString
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import com.sinasamaki.chromadecks.ui.util.ChainedPathEffect

private const val CHAIN_INIT = """pathEffect = PathEffect.chainPathEffect(
    PathEffect.dashPathEffect(
        intervals = floatArrayOf(
            10f, 10f
        )
    ),
    PathEffect.cornerPathEffect(1000f)
)
"""

private const val CHAIN_CORRECTED = """pathEffect = PathEffect.chainPathEffect(
    PathEffect.cornerPathEffect(1000f),
    PathEffect.dashPathEffect(
        intervals = floatArrayOf(
            10f, 10f
        )
    )
)
"""

private fun getChainCode(
    useCornerEffect: Boolean,
    useDashEffect: Boolean,
    useStampEffect: Boolean,
) = """
pathEffect = ChainedPathEffect()
    ${
    if (useCornerEffect)
        """
            .cornerPathEffect(1000f)
        """.trimIndent()
    else ""
}
    ${
    if (useDashEffect)
        """.dashPathEffect(
       intervals = floatArrayOf(100f, 170f)
    )"""
    else ""
}
    ${
    if (useStampEffect)
        """.stampedPathEffect(
        shape = shape,
        advance = 80f, 
        phase = 0f,
        StampedPathEffectStyle.Rotate
    )"""
    else ""
}
    .effect
""".split("\n").filter { it.isNotBlank() }.fastJoinToString("\n")

internal data class ChainPathEffectState(
    val code: String = CHAIN_INIT,
    val showHelperCode: Boolean = false,
    val showAltChainCode: Boolean = false,
    val useCornerEffect: Boolean = false,
    val useDashEffect: Boolean = false,
    val useStampEffect: Boolean = false,
)

internal class ChainPathEffect : ListSlideAdvanced<ChainPathEffectState>() {

    override val initialState: ChainPathEffectState
        get() = ChainPathEffectState()

    override val stateMutations: List<ChainPathEffectState.() -> ChainPathEffectState>
        get() = listOf(
            { copy(code = CHAIN_CORRECTED) },
            { copy(showHelperCode = true) },
            { copy(showHelperCode = false) },
            { copy(showAltChainCode = true) },
            { copy(useCornerEffect = true) },
            { copy(useDashEffect = true) },
            { copy(useStampEffect = true) },
        )

    @Composable
    override fun content(state: ChainPathEffectState) {

        val helperCodeScrim by animateFloatAsState(
            targetValue = if (state.showHelperCode) 1f else 0f,
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .fillMaxSize().blur(
                    radius = lerp(0.dp, 50.dp, helperCodeScrim),
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            CodeBlock(
                modifier = Modifier
                    .weight(1f)
                    .height(560.dp)
                    .padding(vertical = 24.dp)
                    .border(
                        width = 1.dp,
                        color = Neutral700.copy(alpha = .8f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Color(0xff090909),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(16.dp),
                code = if (!state.showAltChainCode)
                    state.code
                else
                    getChainCode(
                        state.useCornerEffect, state.useDashEffect, state.useStampEffect
                    ),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Neutral950)
                        .aspectRatio(1f)
                        .drawCartesianGrid(
                            color = Slate50,
                            alpha = .2f
                        )
                        .clip(RectangleShape)
                        .drawBehind {
                            val path = scalePath(
                                Path().apply {
                                    moveTo(-5f, 80f)
                                    lineTo(40f, 90f)
                                    lineTo(70f, 30f)
                                    lineTo(50f, 10f)
                                    lineTo(20f, 30f)
                                    lineTo(80f, 80f)
                                    lineTo(100f, 90f)
                                }
                            )
                            if (!state.showAltChainCode) {
                                drawPath(
                                    path = path,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Yellow400, Green400
                                        )
                                    ),
                                    style = Stroke(
                                        width = 30f,
                                        pathEffect = when (state.code) {
                                            CHAIN_INIT -> ChainedPathEffect()
                                                .dashPathEffect(
                                                    intervals = floatArrayOf(
                                                        100f,
                                                        170f
                                                    )
                                                )
                                                .cornerPathEffect(1000f)
                                                .effect

                                            else -> ChainedPathEffect()
                                                .cornerPathEffect(1000f)
                                                .dashPathEffect(
                                                    intervals = floatArrayOf(
                                                        100f,
                                                        170f
                                                    )
                                                )
                                                .effect
                                        }
                                    )
                                )
                            } else {
                                drawPath(
                                    path = path,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Yellow400, Green400
                                        )
                                    ),
                                    style = Stroke(
                                        width = 30f,
                                        pathEffect = ChainedPathEffect()
                                            .let {
                                                if (state.useCornerEffect) {
                                                    it.cornerPathEffect(1000f)
                                                } else {
                                                    it
                                                }
                                            }
                                            .let {
                                                if (state.useDashEffect) {
                                                    it.dashPathEffect(
                                                        intervals = floatArrayOf(
                                                            100f,
                                                            170f
                                                        )
                                                    )
                                                } else {
                                                    it
                                                }
                                            }
                                            .let {
                                                if (state.useStampEffect) {
                                                    it.stampedPathEffect(
                                                        shape = Path().apply {
                                                            moveTo(0f, -30f)
                                                            lineTo(0f, 30f)
                                                            lineTo(30f, 0f)

                                                            moveTo(0f, 0f)
                                                            relativeLineTo(0f, -5f)
                                                            relativeLineTo(-50f, 0f)
                                                            relativeLineTo(0f, 10f)
                                                            relativeLineTo(50f, 0f)
                                                        },
                                                        advance = 80f,
                                                        phase = 0f,
                                                        style = StampedPathEffectStyle.Rotate
                                                    )
                                                } else {
                                                    it
                                                }
                                            }
                                            .effect
                                    )
                                )
                            }
                        }
                )
            }

        }


        Box(
            Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.showHelperCode,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.Center)
                    .width(780.dp),
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 10 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 10 }),
            ) {
                CodeBlock(
                    modifier = Modifier
                        .background(
                            color = Neutral950.copy(alpha = .5f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(12.dp)
                        .fillMaxWidth(),
                    code = HELPER_CODE,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            AnimatedVisibility(
                visible = state.showHelperCode,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.CenterEnd),
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            ) {
                Text(
                    text = "↓ Link below ↓",
                    style = MaterialTheme.typography.labelLarge,
                    color = Orange400
                )
            }
        }
    }
}

private const val HELPER_CODE = """class ChainedPathEffect {

    private val effectList = mutableListOf<PathEffect>()
    
    val effect: PathEffect?
        get() = chainEffect(effectList)

    private fun chainEffect(
        effects: List<PathEffect>,
        index: Int = 0,
    ): PathEffect? = when {
        effects.isEmpty() -> null
        index == effectList.lastIndex -> effects.last()
        else -> PathEffect.chainPathEffect(
            chainEffect(effects, index + 1)!!,
            effects[index]
        )
    }

    fun dashPathEffect(intervals: FloatArray, phase: Float = 0f): ChainedPathEffect {
        effectList.add(PathEffect.dashPathEffect(intervals, phase))
        return this
    }

    fun stampedPathEffect(
        shape: Path,
        advance: Float,
        phase: Float,
        style: StampedPathEffectStyle
    ): ChainedPathEffect {
        effectList.add(PathEffect.stampedPathEffect(shape, advance, phase, style))
        return this
    }

    fun cornerPathEffect(radius: Float): ChainedPathEffect {
        effectList.add(PathEffect.cornerPathEffect(radius))
        return this
    }
}
"""