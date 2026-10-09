package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

private const val FINE = .0001f

private const val SPIN_MILLIS = 2800

private const val TOTAL_LOOPS = 4f

private const val FIRST_LOOPS = 2f

internal enum class CirclePhase {
    Spin,

    Slide,

    Ribbon,

    MoreLoops,
}

internal data class CirclePathState(
    val radiusScale: Float,
    val phase: CirclePhase,
    val code: String,
)

internal class CirclePathSlide : ListSlideAdvanced<CirclePathState>() {

    override val initialState: CirclePathState
        get() = CirclePathState(
            radiusScale = .55f,
            phase = CirclePhase.Spin,
            code = UNIT_CODE,
        )

    override val stateMutations: List<CirclePathState.() -> CirclePathState>
        get() = listOf(
            { copy(radiusScale = 1f, code = RADIUS_CODE) },
            { copy(phase = CirclePhase.Slide, code = CENTER_CODE) },
            { copy(phase = CirclePhase.Ribbon, code = RIBBON_CODE) },
            { copy(phase = CirclePhase.MoreLoops) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: CirclePathState) {
        val angle = remember { Animatable(0f, FINE) }
        val center = remember { Animatable(.5f, FINE) }
        val line = remember { Animatable(0f, FINE) }
        val loops = remember { Animatable(1f, FINE) }
        var tracing by remember { mutableStateOf(false) }

        LaunchedEffect(state.phase) {
            when (state.phase) {
                CirclePhase.Spin, CirclePhase.Slide -> {
                    if (tracing) {
                        angle.animateTo(0f, tween(durationMillis = 2000))
                        center.snapTo(0f)
                        tracing = false
                    }
                    val sliding = state.phase == CirclePhase.Slide
                    launch { loops.animateTo(1f, tween(durationMillis = 900)) }
                    launch { line.animateTo(if (sliding) 1f else 0f, tween(durationMillis = 600)) }
                    launch { spin(angle) }

                    if (sliding) {
                        center.animateTo(0f, tween(durationMillis = 900))
                        while (true) {
                            center.animateTo(1f, tween(durationMillis = 2400))
                            center.animateTo(0f, tween(durationMillis = 2400))
                        }
                    } else {
                        center.animateTo(.5f, tween(durationMillis = 900))
                    }
                }

                CirclePhase.Ribbon, CirclePhase.MoreLoops -> {
                    if (!tracing) {
                        angle.snapTo(angle.value % 360f)
                        val target = ceil((angle.value + 90f) / 360f) * 360f
                        val millis = ((target - angle.value) / 360f * SPIN_MILLIS).toInt()
                        coroutineScope {
                            launch { angle.animateTo(target, spring(stiffness = Spring.StiffnessVeryLow)) }
                            launch { center.animateTo(0f, spring(stiffness = Spring.StiffnessVeryLow)) }
                            launch { loops.animateTo(TOTAL_LOOPS, tween(millis)) }
                            launch { line.animateTo(1f, tween(durationMillis = 600)) }
                        }
                        angle.snapTo(0f)
                        tracing = true
                    }
                    val drawn = if (state.phase == CirclePhase.Ribbon) FIRST_LOOPS else TOTAL_LOOPS
                    angle.animateTo(360f * drawn, tween(durationMillis = 2600))
                }
            }
        }

        val radiusScale by animateFloatAsState(
            targetValue = state.radiusScale,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = FINE),
            visibilityThreshold = FINE,
            label = "radiusScale",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1.2f),
                contentAlignment = Alignment.Center,
            ) {
                RibbonDiagram(
                    modifier = Modifier.fillMaxWidth().height(340.dp),
                    sweep = if (tracing) angle.value else angle.value % 360f,
                    loops = loops.value,
                    centerTravel = 1f,
                    centerOverride = if (tracing) null else center.value,
                    travelVisibility = line.value,
                    radiusScale = radiusScale,
                    showTrail = tracing,
                )
            }

            CodePanel(
                code = state.code,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private suspend fun spin(angle: Animatable<Float, AnimationVector1D>) {
    while (true) {
        angle.snapTo(angle.value % 360f)
        val remaining = 360f - angle.value
        angle.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = (remaining / 360f * SPIN_MILLIS).toInt().coerceAtLeast(1),
                easing = LinearEasing,
            ),
        )
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

private val RIBBON_CODE = """
val angle = lerp(first, last, fraction)
val center = lerp(start, end, fraction)

val point = center + Offset(
    x = cos(angle) * radius,
    y = sin(angle) * radius,
)
""".trimIndent()
