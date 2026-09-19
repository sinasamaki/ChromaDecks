package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chromadecks._004_TimelyTimer.components.Timer
import com.sinasamaki.chromadecks._004_TimelyTimer.components.TimelyPlaybackControls
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.FocusZoomer
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.theme.Swatch

private const val TAB_GRADLE = 0
private const val TAB_DIAL = 1
private const val TAB_THUMB = 2
private const val TAB_ARC = 3

private val CENTER_PIVOT = Offset(.5f, .5f)

private val THUMB_PIVOT = Offset(.5f, .2f)

internal data class SetUpState(
    val tab: Int,
    val dialWeight: Float,
    val sweepTurns: Int,
    val zoom: Float,
    val pivot: Offset,
    val thumbStage: Int,
    val thumbScale: Float,
    val timeVisible: Boolean,
    val buttonVisible: Boolean,
)

internal class SetUpSlide : ListSlideAdvanced<SetUpState>() {

    override val initialState: SetUpState
        get() = SetUpState(
            tab = TAB_GRADLE,
            dialWeight = 0f,
            sweepTurns = 1,
            zoom = 1f,
            pivot = CENTER_PIVOT,
            thumbStage = 0,
            thumbScale = 1f,
            timeVisible = false,
            buttonVisible = false,
        )

    override val stateMutations: List<SetUpState.() -> SetUpState>
        get() = listOf(
            { copy(tab = TAB_DIAL, dialWeight = 1f) },
            { copy(sweepTurns = 99) },
            { copy(tab = TAB_THUMB, zoom = 2.8f, pivot = THUMB_PIVOT) },
            { copy(thumbStage = 1) },
            { copy(thumbStage = 2) },
            { copy(thumbStage = 3) },
            { copy(thumbScale = 0f) },
            { copy(thumbScale = 1f) },
            { copy(zoom = 1f, pivot = CENTER_PIVOT) },
            { copy(tab = TAB_ARC, timeVisible = true, buttonVisible = true) },
        )

    @Composable
    override fun content(state: SetUpState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)

        val dialAlpha by animateFloatAsState(
            targetValue = state.dialWeight,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "dialAlpha",
        )
        val sweepDegrees by animateFloatAsState(
            targetValue = state.sweepTurns * 360f,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
            label = "sweepDegrees",
        )
        val thumbScale by animateFloatAsState(
            targetValue = state.thumbScale,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "thumbScale",
        )

        val tabs = listOf(
            "build.gradle.kts" to GRADLE_CODE,
            "TimelyDial.kt" to timelyDialCode(state.sweepTurns),
            "Thumb.kt" to thumbCode(state.thumbStage),
            "Arc.kt" to ARC_CODE,
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
                tabs = tabs,
                selectedTab = state.tab,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                FocusZoomer(
                    zoom = state.zoom,
                    pivot = state.pivot,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .alpha(dialAlpha),
                ) {
                    SetUpDial(
                        swatch = swatch,
                        sweepDegrees = sweepDegrees,
                        thumbStage = state.thumbStage,
                        thumbScale = thumbScale,
                        timeVisible = state.timeVisible,
                        buttonVisible = state.buttonVisible,
                    )
                }
            }
        }
    }
}

@Composable
private fun SetUpDial(
    swatch: Swatch,
    sweepDegrees: Float,
    thumbStage: Int,
    thumbScale: Float,
    timeVisible: Boolean,
    buttonVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val timer = remember { Timer(scope).apply { remainingSeconds = 0f } }

    LaunchedEffect(timer.isRunning) {
        if (timer.isRunning) timer.runCountdown()
    }

    Dial(
        degree = timer.degrees,
        onDegreeChange = timer::onDegreeChange,
        onDegreeChangeFinished = timer::onDegreeChangeFinished,
        sweepDegrees = sweepDegrees,
        enabled = !timer.isRunning,
        modifier = modifier.size(400.dp),
        thumb = {
            SetUpThumb(
                swatch = swatch,
                thumbStage = thumbStage,
                thumbScale = thumbScale,
            )
        },
        track = { dialState ->
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val radius = size.width / 2f - 30.dp.toPx()
                        drawArc(
                            color = swatch.v100.copy(alpha = .3f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            radius = radius,
                        )
                        drawArc(
                            color = swatch.v50,
                            startAngle = 0f,
                            sweepAngle = dialState.degree,
                            radius = radius,
                        )
                    }
            ) {
                if (timeVisible) {
                    TimelyPlaybackControls(
                        timer = timer,
                        swatch = swatch,
                        showButton = buttonVisible,
                    )
                }
            }
        },
    )
}

@Composable
private fun SetUpThumb(
    swatch: Swatch,
    thumbStage: Int,
    thumbScale: Float,
) {
    var modifier = Modifier
        .size(60.dp)
        .scale(thumbScale)

    if (thumbStage >= 1) {
        modifier = modifier.padding(12.5f.dp)
        modifier = if (thumbStage >= 2) {
            modifier
                .border(width = 2.5f.dp, color = swatch.v100, shape = CircleShape)
                .padding(3.75f.dp)
                .background(color = swatch.v100, shape = CircleShape)
        } else {
            modifier.background(color = swatch.v100, shape = CircleShape)
        }
    }

    Box(modifier = modifier) {
        if (thumbStage >= 2) {
            for (i in 0..1) {
                for (j in 0..3) {
                    val index = i * 4 + j
                    val dotScale by animateFloatAsState(
                        targetValue = if (thumbStage >= 3) 1f else 0f,
                        animationSpec = tween(durationMillis = 250, delayMillis = index * 40),
                        label = "gripDot$index",
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-7.5f).dp + (j * 5).dp, y = (-2.5f).dp + (i * 5).dp)
                            .size(3.75f.dp)
                            .scale(dotScale)
                            .background(color = swatch.v500, shape = CircleShape),
                    )
                }
            }
        }
    }
}

private val GRADLE_CODE = """
    dependencies {
      implementation(
        "com.sinasamaki:chroma-dial:1.0.0-Alpha11"
      )
    }
""".trimIndent()

private fun timelyDialCode(sweepTurns: Int) = """
    Dial(
      degree = timer.degrees,
      onDegreeChange = timer::onDegreeChange,
      sweepDegrees = ${if (sweepTurns > 1) "360f * $sweepTurns" else "360f"},
      modifier = Modifier.size(400.dp),
    )
""".trimIndent()

private fun thumbCode(thumbStage: Int) = when {
    thumbStage <= 0 -> """
    thumb = {
    
    }
""".trimIndent()

    thumbStage == 1 -> """
    thumb = {
      Box(
        Modifier
          .size(60.dp)
          .padding(12.5.dp)
          .border(2.5.dp, swatch.v100, CircleShape)
          .background(swatch.v100, CircleShape)
      )
    }
""".trimIndent()

    thumbStage == 2 -> """
    thumb = {
      Box(
        Modifier
          .size(60.dp)
          .padding(12.5.dp)
          .border(2.5.dp, swatch.v100, CircleShape)
          .padding(3.75.dp)
          .background(swatch.v100, CircleShape)
      )
    }
""".trimIndent()

    else -> """
    thumb = {
      Box(
        Modifier
          .size(60.dp)
          .padding(12.5.dp)
          .border(2.5.dp, swatch.v100, CircleShape)
          .padding(3.75.dp)
          .background(swatch.v100, CircleShape)
          .drawBehind {
            // Umm no, these are eight
            for (i in 0..1) {
              for (j in 0..3) {
                drawCircle(
                  color = swatch.v500,
                  radius = 1.875.dp.toPx(),
                  center = gripDotCenter(i, j),
                )
              }
            }
          }
      )
    }
""".trimIndent()
}

private val ARC_CODE = """
    track = {
      Box(
        Modifier
          .fillMaxSize()
          .drawBehind {
            drawArc(
              color = swatch.v100,
              startAngle = 0f,
              sweepAngle = dialState.degree,
              radius = size.width / 2f,
            )

            drawTime()
            drawPlayPauseButton()
          }
      )
    }
""".trimIndent()
