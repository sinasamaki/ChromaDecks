package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sinasamaki.chromadecks._004_TimelyTimer.components.DuckRangeIndicator
import com.sinasamaki.chromadecks._004_TimelyTimer.components.MultiArcDial
import com.sinasamaki.chromadecks._004_TimelyTimer.components.NumberDuckMode
import com.sinasamaki.chromadecks._004_TimelyTimer.components.Timer
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.FocusZoomer
import com.sinasamaki.chromadecks.ui.components.LocalSlideState

private const val BOUNDARY_RANGE_DEGREES = 15f

private val TOP_PIVOT = Offset(.5f, .2f)
private val CENTER_PIVOT = Offset(.5f, .5f)

internal data class MultiArcTransitionState(
    val zoom: Float,
    val pivot: Offset,
    val indicatorVisible: Boolean,
    val rangeExpanded: Boolean,
    val lerped: Boolean,
    val code: String,
)

internal class MultiArcTransitionSlide : ListSlideAdvanced<MultiArcTransitionState>() {

    override val initialState: MultiArcTransitionState
        get() = MultiArcTransitionState(
            zoom = 1f,
            pivot = CENTER_PIVOT,
            indicatorVisible = false,
            rangeExpanded = false,
            lerped = false,
            code = BASE_CODE,
        )

    override val stateMutations: List<MultiArcTransitionState.() -> MultiArcTransitionState>
        get() = listOf(
            { copy(zoom = 2.4f, pivot = TOP_PIVOT) },
            { copy(indicatorVisible = true) },
            { copy(rangeExpanded = true) },
            { copy(lerped = true, code = LERP_CODE) },
        )

    @Composable
    override fun content(state: MultiArcTransitionState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)
        val scope = rememberCoroutineScope()
        val timer = remember { Timer(scope).apply { remainingSeconds = 3400f } }

        LaunchedEffect(timer.isRunning) {
            if (timer.isRunning) timer.runCountdown()
        }

        val indicatorAlpha by animateFloatAsState(
            targetValue = if (state.indicatorVisible) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "indicatorAlpha",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .zIndex(100f)
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                CodeIDE(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = listOf("StackedArcs.kt" to state.code),
                    selectedTab = 0,
                    onTabSelect = {},
                )
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                FocusZoomer(
                    zoom = state.zoom,
                    pivot = state.pivot,
                    modifier = Modifier.aspectRatio(1f).fillMaxSize(),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        MultiArcDial(
                            swatch = swatch,
                            degree = timer.degrees,
                            modifier = Modifier.size(400.dp),
                            onDegreeChange = timer::onDegreeChange,
                            onDegreeChangeFinished = timer::onDegreeChangeFinished,
                            lerpTransition = state.lerped,
                            timer = timer,
                        )
                        DuckRangeIndicator(
                            swatch = swatch,
                            duckMode = if (state.rangeExpanded) NumberDuckMode.POP_RANGE else NumberDuckMode.NONE,
                            symmetric = false,
                            windowDegrees = BOUNDARY_RANGE_DEGREES,
                            alpha = indicatorAlpha,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(400.dp)
                                .graphicsLayer { rotationZ = -90f },
                        )
                    }
                }
            }
        }
    }
}

private fun stackedArcsCode(preamble: String, depth: String) = """
    fun DrawScope.drawStackedArcs(degree: Float) {
      val rings = (degree / 360f).toInt()
${preamble.prependIndent("      ")}
      for (i in 0..rings) {
${depth.prependIndent("        ")}
        val sweep = (degree - i * 360f).coerceAtMost(360f)
        val padding = 12.dp.toPx() * above
        val recency = ((360f - sweep) / 30f).coerceIn(0f, 1f)

        drawArc(
          color = swatch.v100.copy(alpha = 1f - .15f * above),
          startAngle = 0f,
          sweepAngle = sweep,
          radius = center.x - 24.dp.toPx() - padding,
          strokeWidth = lerp(1.dp, 3.dp, recency),
        )
      }
    }
""".trimIndent()

private val BASE_CODE = stackedArcsCode(
    preamble = "",
    depth = "val above = rings - i",
)

private val LERP_CODE = stackedArcsCode(
    preamble = """

    val range = 15f
    val boundary = ((degree % 360f)
      .coerceAtLeast(360f - range) - 360f)
      .absoluteValue / range
    """.trimIndent(),
    depth = "val above = rings - i + 1 - boundary",
)
