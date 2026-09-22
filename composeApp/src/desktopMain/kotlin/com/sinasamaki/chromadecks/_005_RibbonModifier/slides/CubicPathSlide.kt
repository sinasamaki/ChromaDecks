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
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

private const val LOOPS = 3f

internal data class CubicPathState(
    /** The circle sits still until this is set, so the slide can be landed on before it runs. */
    val running: Boolean,
    /** 0 leaves hard corners between the sample points, 1 curves them into the helix. */
    val handleScale: Float,
    val showHandles: Boolean,
    val code: String,
)

/**
 * Beat two: the circle drops a point every quarter turn, those points join up hard-cornered,
 * and then their control handles grow out into the real curve.
 */
internal class CubicPathSlide : ListSlideAdvanced<CubicPathState>() {

    override val initialState: CubicPathState
        get() = CubicPathState(
            running = false,
            handleScale = 0f,
            showHandles = false,
            code = POINTS_CODE,
        )

    override val stateMutations: List<CubicPathState.() -> CubicPathState>
        get() = listOf(
            { copy(running = true) },
            { copy(handleScale = 1f, showHandles = true, code = CUBIC_CODE) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: CubicPathState) {
        // Held at the start until the slide is advanced, then the points land one at a time as
        // the circle reaches them.
        val sweep = remember { Animatable(0f) }
        LaunchedEffect(state.running) {
            if (state.running) sweep.animateTo(360f * LOOPS, tween(durationMillis = 3600))
        }

        val handleScale by animateFloatAsState(
            targetValue = state.handleScale,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
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
                    showHandles = state.showHandles,
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
var degrees = first
while (degrees < last) {
    points += helixPoint(degrees)
    degrees += 90f
}
""".trimIndent()

private val CUBIC_CODE = """
val path = Path()
path.moveTo(helixPoint(first))

var degrees = first
while (degrees < last) {
    val next = degrees + 90f
    path.helixCubicTo(degrees, next)
    degrees = next
}
""".trimIndent()
