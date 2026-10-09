package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.components.CodePanel
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonDiagram
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

private const val LOOPS = 3f

internal data class CubicPathState(
    val running: Boolean,
    val handleScale: Float,
    val code: String,
)

internal class CubicPathSlide : ListSlideAdvanced<CubicPathState>() {

    override val initialState: CubicPathState
        get() = CubicPathState(
            running = false,
            handleScale = 0f,
            code = POINTS_CODE,
        )

    override val stateMutations: List<CubicPathState.() -> CubicPathState>
        get() = listOf(
            { copy(running = true) },
            { copy(handleScale = 1f, code = CUBIC_CODE) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: CubicPathState) {
        val sweep = remember { Animatable(0f) }
        LaunchedEffect(state.running) {
            sweep.animateTo(
                targetValue = if (state.running) 360f * LOOPS else 0f,
                animationSpec = tween(durationMillis = 3600),
            )
        }

        val handleScale by animateFloatAsState(
            targetValue = state.handleScale,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = .0001f),
            label = "handleScale",
        )
        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                RibbonDiagram(
                    modifier = Modifier.fillMaxWidth().height(340.dp),
                    sweep = sweep.value,
                    loops = LOOPS,
                    centerTravel = 1f,
                    handleScale = handleScale,
                    showRadius = false,
                    showSamples = true,
                    showHandles = handleScale > .01f,
                    trailToSamples = true,
                )
            }

            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.2f),
            )
        }
    }
}

private val POINTS_CODE = """
var angle = first
while (angle < last) {
    points += pointAt(angle)
    angle += 90f
}
""".trimIndent()

private val CUBIC_CODE = """
val start = pointAt(first)
path.moveTo(start.x, start.y)

var angle = first
while (angle < last) {
    val next = angle + 90f
    val end = pointAt(next)
    val (c1, c2) = handles(angle, next)
    path.cubicTo(
        c1.x, c1.y,
        c2.x, c2.y,
        end.x, end.y,
    )
    angle = next
}
""".trimIndent()
