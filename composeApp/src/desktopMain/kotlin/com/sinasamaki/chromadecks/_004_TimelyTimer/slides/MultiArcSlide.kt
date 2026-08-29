package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chromadecks._004_TimelyTimer.components.MultiArcDial
import com.sinasamaki.chromadecks._004_TimelyTimer.components.Timer
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.theme.Blue400
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Swatch

private const val TAB_TRACK = 0
private const val TAB_ARCS = 1

internal data class MultiArcState(
    val targetDegree: Float,
    val showArcRow: Boolean,
    val radiusStage: Boolean,
    val styleStage: Boolean,
    val stacked: Boolean,
    val multiRing: Boolean,
    val tab: Int,
    val trackCode: String,
    val arcsCode: String,
)

internal class MultiArcSlide : ListSlideAdvanced<MultiArcState>() {

    override val initialState: MultiArcState
        get() = MultiArcState(
            targetDegree = 0f,
            showArcRow = false,
            radiusStage = false,
            styleStage = false,
            stacked = true,
            multiRing = false,
            tab = TAB_TRACK,
            trackCode = TRACK_SINGLE_ARC_CODE,
            arcsCode = ARCS_ROW_CODE,
        )

    override val stateMutations: List<MultiArcState.() -> MultiArcState>
        get() = listOf(
            { copy(targetDegree = 3.75f * 360f) },
            { copy(showArcRow = true, tab = TAB_ARCS) },
            { copy(stacked = false) },
            { copy(radiusStage = true, arcsCode = ARCS_RADIUS_CODE) },
            { copy(styleStage = true, arcsCode = ARCS_STYLE_CODE) },
            { copy(stacked = true) },
            {
                copy(
                    showArcRow = false,
                    multiRing = true,
                    tab = TAB_TRACK,
                    trackCode = TRACK_FINAL_CODE,
                )
            },
        )

    @Composable
    override fun content(state: MultiArcState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)
        val scope = rememberCoroutineScope()
        val timer = remember { Timer(scope) }

        LaunchedEffect(timer.isRunning) {
            if (timer.isRunning) timer.runCountdown()
        }

        LaunchedEffect(state.targetDegree) {
            Animatable(timer.degrees).animateTo(
                targetValue = state.targetDegree,
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
            ) {
                timer.remainingSeconds = value * 10f
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 72.dp),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = state.showArcRow,
//                    transitionSpec = {
//                        val enter = fadeIn(tween(400)) +
//                                slideInVertically(tween(400)) { h -> if (targetState) h / 4 else -h / 4 }
//                        val exit = fadeOut(tween(400)) +
//                                slideOutVertically(tween(400)) { h -> if (targetState) -h / 4 else h / 4 }
//                        enter togetherWith exit
//                    },
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
//                    modifier = Modifier.fillMaxSize()
                ) { showRow ->
                    if (showRow) {
                        val arcCount = (state.targetDegree / 360f).toInt() + 1
                        MultiArcRow(
                            swatch = swatch,
                            radiusStage = state.radiusStage,
                            styleStage = state.styleStage,
                            stacked = state.stacked,
                            arcCount = arcCount,
                        )
                    } else {
                        MultiArcDial(
                            swatch = swatch,
                            degree = timer.degrees,
                            onDegreeChange = timer::onDegreeChange,
                            onDegreeChangeFinished = timer::onDegreeChangeFinished,
                            timer = timer,
                            multiRing = state.multiRing,
                            lerpTransition = false,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(.5f)
                    .align(Alignment.CenterHorizontally)
            ) {
                CodeIDE(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = listOf(
                        "TimelyDial.kt" to state.trackCode,
                        "StackedArcs.kt" to state.arcsCode,
                    ),
                    selectedTab = state.tab,
                    onTabSelect = {},
                )
            }
        }
    }
}

private val RING_PADDING_STEP = 12.dp

@Composable
private fun MultiArcRow(
    swatch: Swatch,
    radiusStage: Boolean,
    styleStage: Boolean,
    stacked: Boolean,
    arcCount: Int,
    modifier: Modifier = Modifier,
) {
    val sweeps = remember(arcCount) { FloatArray(arcCount) { index -> if (index == 0) 270f else 360f } }

    val radiusProgress = animateFloatAsState(if (radiusStage) 1f else 0f, label = "radiusProgress")
    val styleProgress = animateFloatAsState(if (styleStage) 1f else 0f, label = "styleProgress")
    val stackedProgress = animateFloatAsState(if (stacked) 1f else 0f, label = "stackedProgress")

    Box(
        modifier
            .aspectRatio(1f)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val baseRadius = size.height / 2f
            val ringPadding = RING_PADDING_STEP.toPx()
            val rProgress = radiusProgress.value
            val sProgress = styleProgress.value
            val stProgress = stackedProgress.value

            fun radiusOf(index: Int) =
                androidx.compose.ui.util.lerp(baseRadius, baseRadius - ringPadding * index, rProgress)

            var cursor = 0f
            val rowCenters = FloatArray(arcCount)
            for (index in 0 until arcCount) {
                val diameter = radiusOf(index) * 2f
                rowCenters[index] = cursor + diameter / 2f
                cursor += diameter + ringPadding
            }
            val totalWidth = cursor - ringPadding

            for (index in 0 until arcCount) {
                val recede = if (index == 0) 0f else sProgress
                val offset = androidx.compose.ui.util.lerp(rowCenters[index] - totalWidth / 2f, 0f, stProgress)
                drawArc(
                    color = swatch.v100.copy(alpha = androidx.compose.ui.util.lerp(1f, .45f, recede)),
                    startAngle = 0f,
                    sweepAngle = sweeps[index],
                    radius = radiusOf(index),
                    center = Offset(center.x + offset, center.y),
                    strokeWidth = lerp(3.dp, 1.dp, recede),
                )
            }
        }
    }
}

private fun trackCode(body: String) = """
    track = { dialState ->
      Box(Modifier.fillMaxSize().drawBehind {
${body.prependIndent("        ")}
      })
    }
""".trimIndent()

private val TRACK_SINGLE_ARC_CODE = trackCode(
    """
    drawArc(
      color = swatch.v50,
      startAngle = 0f,
      sweepAngle = dialState.degree.coerceIn(0f, 360f),
      radius = center.x - 24.dp.toPx(),
    )
    """.trimIndent()
)

private val TRACK_FINAL_CODE = trackCode(
    """
    drawTickRing(dialState)
    drawNumbers(dialState)
    drawStackedArcs(dialState.degree)
    """.trimIndent()
)

private fun stackedArcsCode(body: String) = """
    fun DrawScope.drawStackedArcs(degree: Float) {
      val rings = (degree / 360f).toInt()

      for (i in 0..rings) {
${body.prependIndent("        ")}
      }
    }
""".trimIndent()

private val ARCS_ROW_CODE = stackedArcsCode(
    """
    val sweep = (degree - i * 360f).coerceAtMost(360f)

    drawArc(
      color = swatch.v100,
      startAngle = 0f,
      sweepAngle = sweep,
      radius = center.x - 24.dp.toPx(),
    )
    """.trimIndent()
)

private val ARCS_RADIUS_CODE = stackedArcsCode(
    """
    val above = rings - i
    val sweep = (degree - i * 360f).coerceAtMost(360f)
    val padding = 12.dp.toPx() * above

    drawArc(
      color = swatch.v100,
      startAngle = 0f,
      sweepAngle = sweep,
      radius = center.x - 24.dp.toPx() - padding,
    )
    """.trimIndent()
)

private val ARCS_STYLE_CODE = stackedArcsCode(
    """
    val above = rings - i
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
    """.trimIndent()
)
