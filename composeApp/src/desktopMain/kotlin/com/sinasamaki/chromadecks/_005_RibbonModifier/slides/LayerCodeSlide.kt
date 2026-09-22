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
import com.sinasamaki.chromadecks._005_RibbonModifier.FLAT
import com.sinasamaki.chromadecks._005_RibbonModifier.components.CodePanel
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.stagedRibbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

internal data class LayerCodeState(
    val code: String,
    /** Which draw passes have run: 0 behind only, 1 adds the content, 2 adds the front. */
    val stage: Int,
)

/** The modifier itself: drawWithCache, and the two passes either side of drawContent. */
internal class LayerCodeSlide : ListSlideAdvanced<LayerCodeState>() {

    override val initialState: LayerCodeState
        get() = LayerCodeState(code = BEHIND_CODE, stage = 0)

    override val stateMutations: List<LayerCodeState.() -> LayerCodeState>
        get() = listOf(
            { copy(code = CONTENT_CODE, stage = 1) },
            { copy(code = SANDWICH_CODE, stage = 2) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: LayerCodeState) {
        val progress by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            label = "layer-progress",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.25f),
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                HabitRow(
                    modifier = Modifier
                        .width(360.dp)
                        .height(104.dp)
                        .stagedRibbon(
                            colors = FLAT,
                            stroke = 18.dp,
                            loops = 3,
                            stage = state.stage,
                            progress = { progress },
                        ),
                )
            }
        }
    }
}

private val BEHIND_CODE = """
Modifier.drawWithCache {
    val segments = ribbonSegments(size)

    onDrawWithContent {
        segments.forEach {
            if (!it.inFront) {
                drawSegment(it)
            }
        }
    }
}
""".trimIndent()

private val CONTENT_CODE = """
Modifier.drawWithCache {
    val segments = ribbonSegments(size)

    onDrawWithContent {
        segments.forEach {
            if (!it.inFront) {
                drawSegment(it)
            }
        }
        drawContent()
    }
}
""".trimIndent()

private val SANDWICH_CODE = """
Modifier.drawWithCache {
    val segments = ribbonSegments(size)

    onDrawWithContent {
        segments.forEach {
            if (!it.inFront) {
                drawSegment(it)
            }
        }
        drawContent()
        segments.forEach {
            if (it.inFront) {
                drawSegment(it)
            }
        }
    }
}
""".trimIndent()
