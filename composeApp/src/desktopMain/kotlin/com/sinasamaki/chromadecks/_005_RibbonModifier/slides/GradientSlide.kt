package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.CONTRAST
import com.sinasamaki.chromadecks._005_RibbonModifier.SPECTRUM
import com.sinasamaki.chromadecks._005_RibbonModifier.components.CodePanel
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.theme.Rose500

private const val STAGE_SOLID = 0
private const val STAGE_HORIZONTAL = 1
private const val STAGE_SEGMENTS = 2
private const val STAGE_ALONG_PATH = 3

internal data class GradientState(
    val stage: Int,
    val code: String,
)

/** Solid, then a gradient across the whole thing, then a gradient that follows the path. */
internal class GradientSlide : ListSlideAdvanced<GradientState>() {

    override val initialState: GradientState
        get() = GradientState(stage = STAGE_SOLID, code = SOLID_CODE)

    override val stateMutations: List<GradientState.() -> GradientState>
        get() = listOf(
            { copy(stage = STAGE_HORIZONTAL, code = HORIZONTAL_CODE) },
            { copy(stage = STAGE_SEGMENTS, code = SEGMENTS_CODE) },
            { copy(stage = STAGE_ALONG_PATH, code = ALONG_PATH_CODE) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: GradientState) {
        val progress by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800),
            label = "gradient-progress",
        )

        val brush: ((Int) -> Brush)? = when (state.stage) {
            STAGE_SOLID -> { _ -> SolidColor(Rose500) }
            STAGE_HORIZONTAL -> { _ -> Brush.horizontalGradient(SPECTRUM) }
            // One clashing colour per half turn, so the seams are impossible to miss.
            STAGE_SEGMENTS -> { index -> SolidColor(CONTRAST[index % CONTRAST.size]) }
            else -> null
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                HabitRow(
                    modifier = Modifier
                        .width(400.dp)
                        .height(112.dp)
                        .ribbon(
                            colors = SPECTRUM,
                            stroke = 20.dp,
                            loops = 4,
                            progress = { progress },
                            brush = brush,
                        ),
                )
            }

            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.1f),
            )
        }
    }
}

private val SOLID_CODE = """
drawPath(
    path = segment.path,
    brush = SolidColor(Rose500),
)
""".trimIndent()

private val HORIZONTAL_CODE = """
drawPath(
    path = segment.path,
    brush = Brush.horizontalGradient(colors),
)
""".trimIndent()

private val SEGMENTS_CODE = """
val segments = ribbonSegments(size)

segments.forEachIndexed { i, segment ->
    drawPath(
        path = segment.path,
        color = debug[i % debug.size],
    )
}
""".trimIndent()

private val ALONG_PATH_CODE = """
val from = colors.sampleAt(segment.from)
val to = colors.sampleAt(segment.to)

val brush = Brush.verticalGradient(
    0f to from,
    1f to to,
    startY = segment.startY,
    endY = segment.endY,
)
""".trimIndent()
