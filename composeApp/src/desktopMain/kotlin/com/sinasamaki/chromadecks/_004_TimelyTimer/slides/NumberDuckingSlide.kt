package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sinasamaki.chromadecks._004_TimelyTimer.components.DuckRangeIndicator
import com.sinasamaki.chromadecks._004_TimelyTimer.components.DuckingNumberDial
import com.sinasamaki.chromadecks._004_TimelyTimer.components.NumberDuckMode
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.FocusZoomer
import com.sinasamaki.chromadecks.ui.components.LocalSlideState

internal data class NumberDuckingState(
    val numbersVisible: Boolean,
    val zoom: Float,
    val pivot: Offset,
    val duckMode: NumberDuckMode,
    val indicatorVisible: Boolean,
    val code: String,
)

internal class NumberDuckingSlide : ListSlideAdvanced<NumberDuckingState>() {

    override val initialState: NumberDuckingState
        get() = NumberDuckingState(
            numbersVisible = false,
            zoom = 1f,
            pivot = Offset(.5f, .5f),
            duckMode = NumberDuckMode.NONE,
            indicatorVisible = false,
            code = INTERVAL_CODE,
        )

    override val stateMutations: List<NumberDuckingState.() -> NumberDuckingState>
        get() = listOf(
            { copy(numbersVisible = true, code = LABELS_CODE) },
            { copy(zoom = 2.2f, pivot = ZOOMED_PIVOT) },
            { copy(duckMode = NumberDuckMode.SMOOTH) },
            { copy(duckMode = NumberDuckMode.NONE, code = LABELS_CODE) },
            {
                copy(
                    duckMode = NumberDuckMode.POP_EXACT,
                    indicatorVisible = true,
                    code = POP_EXACT_CODE,
                )
            },
            {
                copy(
                    duckMode = NumberDuckMode.POP_RANGE,
                    code = POP_RANGE_CODE,
                )
            },
            {
                copy(
                    duckMode = NumberDuckMode.SMOOTH,
                    code = SMOOTH_CODE,
                )
            },
        )

    @Composable
    override fun content(state: NumberDuckingState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)
        val indicatorAlpha by animateFloatAsState(
            targetValue = if (state.indicatorVisible) 1f else 0f,
            label = "indicatorAlpha",
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier
                    .zIndex(10f)
                    .weight(1f),
                tabs = listOf("DuckingNumber.kt" to state.code),
                selectedTab = 0,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                FocusZoomer(
                    zoom = state.zoom,
                    pivot = state.pivot,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize(),
                ) {
                    DuckingNumberDial(
                        swatch = swatch,
                        duckMode = state.duckMode,
                        numbersVisible = state.numbersVisible,
                    )

                    DuckRangeIndicator(
                        swatch = swatch,
                        duckMode = state.duckMode,
                        alpha = indicatorAlpha,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(400.dp),
                    )
                }
            }
        }
    }
}

private val ZOOMED_PIVOT = Offset(.9f, .5f)

private val INTERVAL_CODE = """
    drawEveryInterval(
      interval = 90f,
      radius = size.width * .385f,
      orientation = IntervalOrientation.PositionOnly,
    ) { data ->

    }
""".trimIndent()

private val LABELS_CODE = """
    drawEveryInterval(
      interval = 90f,
      radius = size.width * .385f,
      orientation = IntervalOrientation.PositionOnly,
    ) { data ->
      if (data.index == 0) return@drawEveryInterval
      drawText(
        text = "${'$'}{data.index * 15}",
      )
    }
""".trimIndent()

private fun duckingCode(push: String) = """
    drawEveryInterval(
      interval = 90f,
      radius = size.width * .385f,
      orientation = IntervalOrientation.PositionOnly,
    ) { data ->
      if (data.index == 0) return@drawEveryInterval
${push.prependIndent("      ")}
      val inward = (center - data.position) / radius

      drawText(
        text = "${'$'}{data.index * 15}",
        topLeft = inward * (duckDistance * push),
      )
    }
""".trimIndent()

private val POP_EXACT_CODE = duckingCode(
    """
    val ducked = degree.toInt() ==
      data.intervalDegree.toInt()
    val push = if (ducked) 1f else 0f
    """.trimIndent()
)

private val POP_RANGE_CODE = duckingCode(
    """
    val ducked = degree.toInt() in
      (data.intervalDegree - window).toInt()..
      (data.intervalDegree + window).toInt()
    val push = if (ducked) 1f else 0f
    """.trimIndent()
)

private val SMOOTH_CODE = duckingCode(
    """
    val distance =
      (degree - data.intervalDegree).absoluteValue
    val push = (1f - distance / window)
      .coerceIn(0f, 1f)
    """.trimIndent()
)
