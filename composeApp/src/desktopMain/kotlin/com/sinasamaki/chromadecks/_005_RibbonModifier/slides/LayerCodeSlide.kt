package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

internal data class LayerCodeState(
    val code: String,
    val behind: Boolean,
    val content: Boolean,
    val front: Boolean,
)

internal class LayerCodeSlide : ListSlideAdvanced<LayerCodeState>() {

    override val initialState: LayerCodeState
        get() = LayerCodeState(code = EMPTY_CODE, behind = false, content = false, front = false)

    override val stateMutations: List<LayerCodeState.() -> LayerCodeState>
        get() = listOf(
            { copy(code = SEGMENTS_CODE) },
            { copy(code = BEHIND_CODE, behind = true) },
            { copy(code = CONTENT_CODE, content = true) },
            { copy(code = SANDWICH_CODE, front = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: LayerCodeState) {
        val behindProgress by animateFloatAsState(
            targetValue = if (state.behind) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = .0001f),
            label = "layer-behind",
        )
        val frontProgress by animateFloatAsState(
            targetValue = if (state.front) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = .0001f),
            label = "layer-front",
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
                        .width(400.dp)
                        .height(116.dp)
                        .stagedRibbon(
                            colors = FLAT,
                            stroke = 18.dp,
                            loops = 3,
                            content = state.content,
                            behindProgress = { behindProgress },
                            frontProgress = { frontProgress },
                        ),
                )
            }
        }
    }
}

private val EMPTY_CODE = """
Modifier.drawWithCache {

    onDrawWithContent {

    }
}
""".trimIndent()

private val SEGMENTS_CODE = """
Modifier.drawWithCache {
    val segments = ribbonSegments(size)

    onDrawWithContent {

    }
}
""".trimIndent()

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
