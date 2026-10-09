package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.RIBBON_COLORS
import com.sinasamaki.chromadecks._005_RibbonModifier.components.CodePanel
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

internal data class RibbonRevealState(
    val progress: Float,
    val sequential: Boolean,
    val millis: Int,
    val code: String,
)

private const val TOGETHER_MILLIS = 900

private const val SEQUENTIAL_MILLIS = 4200

internal class RibbonRevealSlide : ListSlideAdvanced<RibbonRevealState>() {

    override val initialState: RibbonRevealState
        get() = RibbonRevealState(progress = 0f, sequential = false, millis = TOGETHER_MILLIS, code = DASH_CODE)

    override val stateMutations: List<RibbonRevealState.() -> RibbonRevealState>
        get() = listOf(
            { copy(progress = 1f) },
            { copy(progress = 0f) },
            { copy(progress = 1f, sequential = true, millis = SEQUENTIAL_MILLIS, code = SLICE_CODE) },
            { copy(progress = 0f) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: RibbonRevealState) {
        val progress by animateFloatAsState(
            targetValue = state.progress,
            animationSpec = tween(durationMillis = state.millis, easing = FastOutSlowInEasing),
            label = "reveal-progress",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.2f),
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                HabitRow(
                    modifier = Modifier
                        .width(440.dp)
                        .height(124.dp)
                        .ribbon(
                            colors = RIBBON_COLORS,
                            stroke = 20.dp,
                            loops = 4,
                            progress = { progress },
                            sequential = state.sequential,
                        ),
                )
            }
        }
    }
}

private val DASH_CODE = """
val reveal = segment.length * progress
val hide = segment.length

pathEffect = PathEffect.dashPathEffect(
    floatArrayOf(reveal, hide)
)
""".trimIndent()

private val SLICE_CODE = """
val span = segment.to - segment.from
val local = (
    (progress - segment.from) / span
).coerceIn(0f, 1f)

val reveal = segment.length * local
val dash = PathEffect.dashPathEffect(
    floatArrayOf(reveal, segment.length)
)

drawPath(
    path = segment.path,
    brush = segment.brush,
    style = Stroke(
        width = stroke,
        cap = StrokeCap.Round,
        pathEffect = dash,
    ),
)
""".trimIndent()
