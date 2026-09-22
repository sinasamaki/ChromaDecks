package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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

internal data class CirclePathState(
    val radiusScale: Float,
    /** The point keeps going round on its own, with nothing being drawn behind it. */
    val spinning: Boolean,
    /** The circle slides back and forth, so the travel reads on its own before it is combined. */
    val sliding: Boolean,
    val loops: Float,
    val centerTravel: Float,
    val showTrail: Boolean,
    val code: String,
)

/** Beat one: a point on a circle, a radius, a circle that travels, and the helix from all three. */
internal class CirclePathSlide : ListSlideAdvanced<CirclePathState>() {

    override val initialState: CirclePathState
        get() = CirclePathState(
            radiusScale = .55f,
            spinning = true,
            sliding = false,
            loops = 1f,
            centerTravel = 0f,
            showTrail = false,
            code = UNIT_CODE,
        )

    override val stateMutations: List<CirclePathState.() -> CirclePathState>
        get() = listOf(
            { copy(radiusScale = 1f, code = RADIUS_CODE) },
            { copy(sliding = true, code = CENTER_CODE) },
            {
                copy(
                    spinning = false,
                    sliding = false,
                    loops = 3f,
                    centerTravel = 1f,
                    showTrail = true,
                    code = HELIX_CODE,
                )
            },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: CirclePathState) {
        val endless = rememberInfiniteTransition(label = "circle-endless")

        val spin by endless.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing)),
            label = "spin",
        )
        val slide by endless.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "slide",
        )

        // The helix draws itself in on arrival rather than snapping to a finished path.
        val sweep = remember { Animatable(0f) }
        LaunchedEffect(state.loops, state.spinning) {
            if (!state.spinning) sweep.animateTo(360f * state.loops, tween(2600))
        }

        val radiusScale by animateFloatAsState(
            targetValue = state.radiusScale,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
            label = "radiusScale",
        )
        val loops by animateFloatAsState(
            targetValue = state.loops,
            animationSpec = tween(durationMillis = 1600),
            label = "loops",
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
                    sweep = if (state.spinning) spin else sweep.value,
                    loops = loops,
                    centerTravel = state.centerTravel,
                    centerOverride = if (state.sliding) slide else null,
                    radiusScale = radiusScale,
                    showTrail = state.showTrail,
                )
            }

            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1.2f),
            )
        }
    }
}

private val UNIT_CODE = """
val point = Offset(
    x = cos(angle),
    y = sin(angle),
)
""".trimIndent()

private val RADIUS_CODE = """
val point = Offset(
    x = cos(angle) * radius,
    y = sin(angle) * radius,
)
""".trimIndent()

private val CENTER_CODE = """
val center = lerp(start, end, fraction)

val point = center + Offset(
    x = cos(angle) * radius,
    y = sin(angle) * radius,
)
""".trimIndent()

private val HELIX_CODE = """
val angle = lerp(first, last, fraction)
val center = lerp(start, end, fraction)

val point = center + Offset(
    x = cos(angle) * radius,
    y = sin(angle) * radius,
)
""".trimIndent()
