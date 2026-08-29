package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sinasamaki.chromadecks._004_TimelyTimer.components.RingLinesDial
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.FocusZoomer
import com.sinasamaki.chromadecks.ui.components.LocalSlideState

private val TOP_RIGHT_PIVOT = Offset(.8f, .2f)
private val CENTER_PIVOT = Offset(.5f, .5f)

internal data class RingLinesState(
    val ringVisible: Boolean,
    val rangeVisible: Boolean,
    val stretchVisible: Boolean,
    val zoom: Float,
    val pivot: Offset,
    val code: String,
)

internal class RingLinesSlide : ListSlideAdvanced<RingLinesState>() {

    override val initialState: RingLinesState
        get() = RingLinesState(
            ringVisible = false,
            rangeVisible = false,
            stretchVisible = false,
            zoom = 1f,
            pivot = CENTER_PIVOT,
            code = EMPTY_INTERVAL_CODE,
        )

    override val stateMutations: List<RingLinesState.() -> RingLinesState>
        get() = listOf(
            { copy(ringVisible = true, code = RING_CODE) },
            { copy(zoom = 2.6f, pivot = TOP_RIGHT_PIVOT) },
            { copy(rangeVisible = true) },
            { copy(stretchVisible = true, code = RANGE_CODE) },
        )

    @Composable
    override fun content(state: RingLinesState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier
                    .zIndex(10f)
                    .weight(1f),
                tabs = listOf("TimelyDial.kt" to state.code),
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
                    modifier = Modifier.aspectRatio(1f).fillMaxHeight(),
                ) {
                    RingLinesDial(
                        swatch = swatch,
                        ringVisible = state.ringVisible,
                        rangeVisible = state.rangeVisible,
                        stretchVisible = state.stretchVisible,
                    )
                }
            }
        }
    }
}

private fun ringCode(body: String) = """
    drawEveryInterval(
      startDegrees = 0f,
      sweepDegrees = 360f,
      interval = 6f,
      radius = size.width / 2f,
      currentDegree = dialState.degree,
      orientation = IntervalOrientation.PositionAndRotate,
    ) { data ->
${body.prependIndent("      ")}
    }
""".trimIndent()

private val EMPTY_INTERVAL_CODE = ringCode("")

private val RING_CODE = ringCode(
    """
    drawLine(
      color = swatch.v100,
      start = Offset(0f, 12.dp.toPx()),
      end = Offset(0f, 12.dp.toPx() - 10.dp.toPx()),
      strokeWidth = 2.dp.toPx(),
    )
    """.trimIndent()
)

private val RANGE_CODE = ringCode(
    """
    val delta = data.intervalDegree - dialState.degree
    val x = 1f - (delta.absoluteValue / 18f).coerceIn(0f, 1f)
    val height = lerp(10.dp.toPx(), 40.dp.toPx(), x)

    drawLine(
      color = if (data.inActiveRange) swatch.v50 else swatch.v100,
      start = Offset(0f, 12.dp.toPx()),
      end = Offset(0f, 12.dp.toPx() - height),
      strokeWidth = 2.dp.toPx(),
    )
    """.trimIndent()
)
